# Power Management Simulation System 
Group Members: Sayumi Amarasinghe, Ashley Mapes, Sarah Huynh, Dorsey Yan, Cesar Gomez, Ryan Miller 

## Project Description: 
This project simulates the energy management of an entire building; the building has different rooms each with many appliances.
- An appliances can be smart or regular
- Smart appliances have low, on, and off states
- Regular appliances have on and off states
- Each state has a specific amount of energy it uses

## Input
  
- The user enters the total wattage that the building can have. 
- Our system will turn off the least amount of appliances possible to meet the power limit

## Limitations
- Appliances can only be turned "off" by turning every appliance in the room "off"

## Execution

1. Run GeneralClient.java. 
2. There will be a menu that prompts the user to:
	Add an appliance(A), Delete an appliance (D), List the appliances (L),
	Read appliances from a file (F), start the simulation (S) or Quit the Program(Q)
3. Before starting the simulation, press L or F; if reading appliances from a file, copy 
   the file path to execute it. 
4. To start the simulation, input the maximum limit for the wattage and the number 
	of timesteps. 
