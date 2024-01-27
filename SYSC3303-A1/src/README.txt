SYSC3303 - Assignment 1

In this Assignment I've included 4 Java files, Agent, Chef, Table and Main.

Agent.java - Contains the source code for the Agent Class, which for all purposes is our "Producer" Class

Table.java - Contains the souurce code for the Table Class, this is essentially our 1 Integer long buffer, with 3 Methods, add, take and check.
	     These methods set the value in the buffer if the empty flag is true, save and remove the current value from the buffer if the empty flag is false,
             and simply return the current value in the buffer for our Chefs to check whats on the table.	

Chef.java - Contains the source code for the Chef Class, this is our "Consumer" equivalent in this problem. It peeks at the value on the table and based on which ingredient
            it was given unlimited amounts of at creation, it will take the value from the buffer, make the sandwich and eat it.
