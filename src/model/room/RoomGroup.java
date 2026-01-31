package model.room;

import java.util.*;

    public class RoomGroup implements RoomComponent {
        
        private String name_of_groupe;
        private List<RoomComponent> rooms ; 

         
        //building the constructor 
        public RoomGroup (String name_of_groupe){
            this.name_of_groupe = name_of_groupe ;
            this.rooms = new ArrayList<>();
        }

         
        //adding room
        public void addComponent(RoomComponent component){
            rooms.add(component) ; 
        }

        //removing one
        public void removeComponent(RoomComponent component){
            rooms.remove(component) ; 
        }


        @Override 
        public int getId(){
            return -1 ; //no id for a groupe of rooms 
        }

        @Override 
        public int getNumber(){
            return -1 ; 
        }
        @Override 
        public String getType(){
            return "Groupe "+ name_of_groupe ; 
        }

        @Override 
        public int getPrice(){
            int total_price = 0 ; 

            for(RoomComponent component : rooms){
                total_price += component.getPrice();
            }

            return total_price ; 
        }

        //features of the groupe room we put the features of each room of the groupe
        @Override 
        public List<String> getFeatures(){
            Set<String> allfeatures  = new HashSet<>();

            for(RoomComponent room : rooms){
              allfeatures.addAll(room.getFeatures()) ;
            }

             return new ArrayList<>(allfeatures);
        }


         @Override
    public String getState() {
      
        if (rooms.isEmpty()) {
            return "empty";
        }
        
        boolean everythingavaibale = true;
        boolean everythingoccupied = true;
        

        for (RoomComponent element : rooms) {
            String etatDeElement = element.getState();
            
            if (!etatDeElement.equalsIgnoreCase("available")) {
                everythingavaibale = false;
            }
            
        
            if (!etatDeElement.equalsIgnoreCase("occupied")) {
                everythingoccupied = false;
            }
        }
        
        if (everythingavaibale) {
            return "available";
        } else if (everythingoccupied) {
            return "occupied";
        } else {
            return "Partially occupied";
        }
    }


    public void display_room_groupe(){

        System.out.println("\n  ROOM GROUPE: " + name_of_groupe.toUpperCase());
        System.out.println(" Total price: " + getPrice() + " DA");
        System.out.println(" state: " + getState());
        System.out.println(" features: " + getFeatures());
        System.out.println(" number of rooms inside it  (" + rooms.size() + " room):");



        for (RoomComponent room : rooms) {
             //here cuz the Roomconpoentn can be either room or a groupe ( roomcomponetn is an interface that both roommodel and roomcomponent heritate)
            if (room instanceof RoomModel) {
                //give the type roommodel to room
                RoomModel rOom = (RoomModel) room;
                System.out.println(" room " + rOom.getNumber() + " - " + rOom.getType() + " - " + rOom.getPrice() + " DA");
            } 
            // if it is a roomgroupe
            else if (room instanceof RoomGroup) {
                RoomGroup subgroup = (RoomGroup) room;
                System.out.println(" : " + subgroup.getName() + " (" + subgroup.rooms.size() + " room)");
            }
        }
    }
    

    public String getName() {
        return name_of_groupe;
    }
    

    public List<RoomComponent> getElements() {
        return new ArrayList<>(rooms);
    }
}





