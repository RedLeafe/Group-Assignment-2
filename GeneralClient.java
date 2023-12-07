import java.io.File;
import java.util.*;


public class GeneralClient {
    public static void runSimulation( int timeStep, double allowedWattage, ArrayList<Appliance> totalAppliances){
        ArrayList<Appliance> regAppList = new ArrayList<>();
        ArrayList<SmartAppliance> smartAppList = new ArrayList<>();
        ArrayList<Integer> locationsAffected = new ArrayList<>();
        // seperate appliances into smart and regular
        for(int i = 0; i < totalAppliances.size(); i++){
            if( totalAppliances.get(i) instanceof SmartAppliance ){
                smartAppList.add( (SmartAppliance)totalAppliances.get(i));
            }
            else{
                regAppList.add( totalAppliances.get(i));
            }
        }

        // order smartAppList
        for (int i = smartAppList.size() - 1; i > 0; i--)
        {
            for (int j = 0; j < i; j++)
            {
                if (smartAppList.get(j).getOutput() > smartAppList.get(j + 1).getOutput())
                {
                    SmartAppliance temp = smartAppList.get(j);
                    smartAppList.set(j, smartAppList.get(j+1));
                    smartAppList.set(j+1,temp);
                }
            }
        }

        int t = 0;
        while( t < timeStep )
        {
            boolean isInList = false;
            int roomNum;

            locationsAffected.clear();
            System.out.println( "Time step " + (t+1) + ":");
            int numSmartAppliancesLow = 0;
            int numLocationsBrowedOut = 0;

            randomizeOn(regAppList, smartAppList);

            double totalPower = calcTotalWatt(regAppList, smartAppList);

            // step 1: order smart appliances in increasing order
            for (int i = smartAppList.size() - 1; i > 0; i--)
            {
                for (int j = 0; j < i; j++)
                {
                    if (smartAppList.get(j).getPowerOn() > smartAppList.get(j + 1).getPowerOn())
                    {
                        SmartAppliance temp = new SmartAppliance(smartAppList.get(j));
                        smartAppList.set(j, new SmartAppliance(smartAppList.get(j+1)));
                        smartAppList.set(j+1,new SmartAppliance(temp));
                    }
                }
            }

            int count = smartAppList.size()-1; //start from highest appliance power
            while( totalPower > allowedWattage && count >= 0){
                if( smartAppList.get(count).isOn() ){
                    smartAppList.get(count).turnLow();
                    totalPower -= (smartAppList.get(count).getPowerOn() - smartAppList.get(count).getOutput());
                    numSmartAppliancesLow++;
                    roomNum = smartAppList.get(count).getLocationID();
                    for (int i = 0; i < locationsAffected.size(); i++)
                    {
                        if (locationsAffected.get(i) == roomNum ){
                            isInList = true;
                        }
                    }
                    if( !isInList ){
                        locationsAffected.add(roomNum);
                    }
                    isInList = false;
                    
                }
                count--;
            }
            
            // step 2: put appliances in rooms then order rooms based on size
            ArrayList<Room> rooms = putIntoRooms(regAppList, smartAppList);
            if( totalPower > allowedWattage ){
                for (int i = rooms.size() - 1; i > 0; i--){
                    for (int j = 0; j < i; j++)
                    {
                        if (rooms.get(j).totalWattage() > rooms.get(j + 1).totalWattage())
                        {
                            Room temp = rooms.get(j);
                            rooms.set(j, rooms.get(j+1));
                            rooms.set(j + 1, temp);
                        }
                    }
                }

                count = 0;
                while (totalPower > allowedWattage && count < rooms.size())
                {
                    
                    rooms.get(count).brownOut();
                    numLocationsBrowedOut++;
                    roomNum = rooms.get(count).getRoomNum();
                    count++;
                    
                    for (int i = 0; i < locationsAffected.size(); i++)
                    {
                        if (locationsAffected.get(i) == roomNum)
                            isInList = true;
                    }
                     
                    if( !isInList ){
                        locationsAffected.add(roomNum);
                    }
                    isInList = false;
                    
                }
            }
            turnAllOff(regAppList, smartAppList);
            System.out.println("Number of appliances turned to low: " + numSmartAppliancesLow);
            System.out.println("Number of locations browned out: " + numLocationsBrowedOut);
            System.out.println("Number of locations affected: " + locationsAffected.size());
            System.out.println();
            t++;
            
        }
    }


    public static double calcTotalWatt( ArrayList<Appliance> regApp, ArrayList<SmartAppliance> smartApp ){
        double total = 0.0;
        for( int i = 0; i < regApp.size(); i++){
            if( regApp.get(i).isOn())
                total += regApp.get(i).getOutput();
        }
        for( int i = 0; i < smartApp.size(); i++){
            if( smartApp.get(i).isOn())
                total += regApp.get(i).getOutput();
        }
        return total;
    }

    public static void randomizeOn( ArrayList<Appliance> regApp, ArrayList<SmartAppliance> smartApp ){
        for( int i = 0; i < regApp.size(); i++){
            regApp.get(i).tryTurnOn();
        }
        for( int i = 0; i < smartApp.size(); i++){
            smartApp.get(i).tryTurnOn();
        }
    }

    public static void turnAllOff( ArrayList<Appliance> regApp, ArrayList<SmartAppliance> smartApp ){
        for( int i = 0; i < regApp.size(); i++){
            regApp.get(i).turnOff();
        }
        for( int i = 0; i < smartApp.size(); i++){
            smartApp.get(i).turnOff();
        }
    }

    public static ArrayList<Room> putIntoRooms( ArrayList<Appliance> regApp, ArrayList<SmartAppliance> smartApp ){
        ArrayList<Room> rooms = new ArrayList<>();
        for( int i = 0; i < 100; i++ ){
            rooms.add( new Room() );
            rooms.get(i).setRoomNum(10000000 + i);
        }
        
        for( int i = 0; i < regApp.size(); i++){
            rooms.get( regApp.get(i).getLocationID() - 10000001 ).addAppliance(regApp.get(i));
        }

        for( int i = 0; i < smartApp.size(); i++){
            rooms.get( smartApp.get(i).getLocationID() - 10000001 ).addAppliance(smartApp.get(i));
        }

        for( int i = 0; i < rooms.size(); i++){
            if( rooms.get(i).getNumAppliances() == 0 ){
                rooms.remove(i);
                i--;
            }
        }

        return rooms;
    }

    public static void main( String[] args )
    {
        Scanner scnr = new Scanner( System.in );
        ArrayList<Appliance> totalAppliances = new ArrayList<>();

        String option1;
        while(true){// Application menu to be displayed to the user.
			System.out.println("Select an option:");
			System.out.println("Type \"A\" Add an appliance");
			System.out.println("Type \"D\" Delete an appliance");	
			System.out.println("Type \"L\" List the appliances");
			System.out.println("Type \"F\" Read Appliances from a file");
			System.out.println("Type \"S\" To Start the simulation");
			System.out.println("Type \"Q\" Quit the program");
            option1=scnr.nextLine();
            if( option1.equals( "A")){
                
                    System.out.println( "Please enter an appliance:");
                    StringTokenizer stringToken = new StringTokenizer(scnr.nextLine());
                    int locationID = Integer.parseInt(stringToken.nextToken(","));
                    String description = stringToken.nextToken(",");
                    double wattage = Double.parseDouble(stringToken.nextToken(","));
                    double probOn = Double.parseDouble(stringToken.nextToken(","));
                    boolean type = Boolean.parseBoolean(stringToken.nextToken(","));
                    double low = Double.parseDouble(stringToken.nextToken());
                    if( type == false)
                    {
                        totalAppliances.add( new Appliance(locationID, description, wattage, probOn) );
                    }
                    else{
                        totalAppliances.add( new SmartAppliance(locationID, description, low, probOn, low));
                    }
                

            }
            else if( option1.equals( "D")){
                totalAppliances.remove(totalAppliances.size()-1);
            }
            else if( option1.equals( "L")){
                for( int i = 0; i < totalAppliances.size(); i++){
                    System.out.println(totalAppliances.get(i));
                }
            }
            else if( option1.equals( "F")){
                try{
                    System.out.println( "Please input the name of the input file: ");
                    String nameInputFile = scnr.nextLine();
                    File inputFile = new File( nameInputFile );
                    Scanner scan = new Scanner( inputFile );
                    while ( scan.hasNext( ) ) {
                        StringTokenizer stringToken = new StringTokenizer(scan.nextLine());
                        int locationID = Integer.parseInt(stringToken.nextToken(","));
                        String description = stringToken.nextToken(",");
                        double wattage = Double.parseDouble(stringToken.nextToken(","));
                        double probOn = Double.parseDouble(stringToken.nextToken(","));
                        boolean type = Boolean.parseBoolean(stringToken.nextToken(","));
                        double low = Double.parseDouble(stringToken.nextToken());
                        if( type == false)
                        {
                            totalAppliances.add( new Appliance(locationID, description, wattage, probOn) );
                        }
                        else{
                            totalAppliances.add( new SmartAppliance(locationID, description, low, probOn, low));
                        }
                    }
                    scan.close();
                }
                catch( Exception e){
                    System.out.println( e.getMessage());
                }
            }
            else if( option1.equals( "S")){
                boolean isRunning = true;
                int timeStep = 0;
                double limit = 0.0;
                while(isRunning){
                    try{
                        System.out.println("Enter time steps:");
                        timeStep = Integer.parseInt(scnr.next());
                        if( timeStep <= 0){
                            throw new Exception();
                        }
                        isRunning = false;
                    }
                    catch( NumberFormatException e ){
                        System.out.println( "ERROR: input must be an integer");
                    }
                    catch( Exception ex ){
                        System.out.println( "ERROR: input must be greater than 0" );
                    }
                }
                isRunning = true;
                while(isRunning){
                    try{
                        System.out.println("Enter the total allowed wattage:");
                        limit = Double.parseDouble(scnr.next());
                        if( limit < 0){
                            throw new Exception();
                        }
                        isRunning = false;
                    }
                    catch( NumberFormatException e ){
                        System.out.println( "ERROR: input must be an number\n");
                    }
                    catch( Exception ex ){
                        System.out.println( "ERROR: input must be greater than or equal to 0\n" );
                    }
                }
                
                runSimulation(timeStep, limit, totalAppliances);
                break;
            }
            else if( option1.equals( "Q")){
                break;
            }
            else{
                System.out.println( "ERROR: must input one of available options");
            }
            System.out.println();
				
		}
        System.out.println( "End of simulation.");
        scnr.close();


		


    }
    
}


