# Power Usage Simulation System 
Group Members: Sayumi Amarasinghe, Ashley Mapes, Sarah Huynh, Dorsey Yan, Cesar Gomez, Ryan Miller 

Project Description: This project simulates the power usage of a building; the building has different rooms and two possible appliances, smart or regular. 
Smart appliances have low, on, and off states, while regular appliances have on and off states. 
The user enters the total wattage that the building can have. The goal of the project is to turn off the least amount of appliances to meet the power limit 

The Client Class in our program is GeneralClient.java. 

To execute our program: 
1. Run GeneralClient.java. 
2. There will be a menu that prompts the user to:
	Add an appliance(A), Delete an appliance (D), List the appliances (L),
	Read appliances from a file (F), start the simulation (S) or Quit the Program(Q)
3. Before starting the simulation, press L or F; if reading appliances from a file, copy 
   the file path to execute it. 
4. To start the simulation, input the maximum limit for the wattage and the number 
	of timesteps. 
