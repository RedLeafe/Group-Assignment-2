import java.util.*;
public class Room {
    ArrayList<Appliance> appliances;
    boolean isBrownedOut;
    int roomNum = 0;

    public Room(){
        appliances = new ArrayList<Appliance>();
        isBrownedOut = false;
        roomNum = 0;
    }

    public void setRoomNum( int num ){
        this.roomNum = num;
    }

    public void addAppliance( Appliance app ){
        if( app instanceof SmartAppliance){
            appliances.add( new SmartAppliance((SmartAppliance)app) );
        }
        else{ 
            appliances.add( new Appliance(app));
        }

    }

    public ArrayList<Appliance> getAppliances(){
        return appliances;
    }

    public void brownOut(){
        for( int i = 0; i < appliances.size(); i++){
            appliances.get(i).turnOff();
        }
    }


    public int getNumAppliances(){
        return appliances.size();
    }

    public boolean getStatus(){
        return isBrownedOut;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public double totalWattage(){
        double sum = 0.0;
        for( int i = 0; i < appliances.size(); i++){
            sum += appliances.get(i).getOutput();
        }

        return sum;
    }

    public int isAffected(){
        for( int i = 0; i < appliances.size(); i++){
            if(appliances.get(i) instanceof SmartAppliance){
                if (((SmartAppliance)(appliances.get(i))).isLow()){
                    return 1;
                }
            }
        }
        return 0;
    }

}
