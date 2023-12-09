public class SmartAppliance extends Appliance {
    private double lowPercent;
    private boolean isLow;


    public SmartAppliance(int locationID, String description, double powerOn, double probOn, double lowPercent) {
        super(locationID, description, powerOn, probOn);
        this.lowPercent = lowPercent;
    }

    public SmartAppliance( SmartAppliance app ){
        super(app.getLocationID(), app.getDescription(), app.getPowerOn(), app.getProbOn());
        this.lowPercent = app.getLow();
        this.isLow = app.isLow;
        super.setStatus(app.isOn() );
        isLow = app.isLow();
    }

    // getter to copy stuff
    public double getLow() {
        return this.lowPercent;
    }

    public double getOutput()
    {
        if(isLow)
            return (super.getOutput() * (1-lowPercent));
        else
            return super.getOutput();
        
    }

    public void turnOn(){
        super.turnOn();
        isLow = false;
    }

    public void turnOff(){
        super.turnOff();
        isLow = false;
    }

    public void turnLow(){
        super.turnOn();
        isLow = true;
    }

    public String toString(){
        return (super.getLocationID() + "," + super.getDescription() + "," + super.getPowerOn() + "," + super.getProbOn() + "," + "true," + lowPercent );
    }
    public boolean isLow(){
        return isLow;
    }
}
