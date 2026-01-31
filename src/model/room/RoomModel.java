package model.room;

import java.util.List;

public class RoomModel implements RoomComponent {
    private int id ; 
    private int number ; 
    private String type ; 
    private int price ; 
    private List<String> features ; 
    private String state ;



    public RoomModel(){
        // for gson
    }

public RoomModel(int id , int number , String type , int price , List<String> features , String state){
    this.id = id ;
    this.number = number ;
    this.type = type ; 
    this.price = price ; 
    this.features = features ; 
    this.state = state ; 
}


@Override 
public int getId(){
    return this.id ; 
}

@Override 
public int getNumber(){
    return this.number ; 
}

@Override 
public String getType(){
    return this.type ; 
}

@Override 
public List<String> getFeatures(){
    return this.features ; 
}

@Override 
public String getState(){
  return this.state ; 
}

@Override 
public int getPrice(){
    return this.price ; 
}

public void display(){
    System.out.println(" | THE ROOM N° " + number + "  | ID : " +id + " | PRICE : " +price+ " | TYPE : " +type+ " | FEATURES " +features+ " | STATE : " +state);
}

    @Override
    public String toString() {
        return "RoomModel{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}';
    }

    public void setState (String state){
        this.state = state ;
    }

    public void setPrice(int price){
        this.price = price ;
    }

    public void setNumber (int number ){
        this.number = number ;
    }

    public void setFeatures(List<String> features){
         this.features = features ;
    }

    public void setId (int id ){
        this.id = id ;
    }

    public void setType (String type){
        this.type = type ;
    }




}
