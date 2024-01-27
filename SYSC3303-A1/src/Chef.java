/**
 * Consumer is the class for the consumer thread. CHEFS
 */
class Chef implements Runnable
{
    private Table buffer;
    private int ing;
    int[] ingredient = {6,5,3}; //The ingredient list that holds the numbers corresponding to the ingredient combinations
    String[] ingname = {"Peanut Butter and Bread","Bread and Jam","Peanut Butter and Jam"}; //Array to hold the string outputs based on what is on the Table
    public Chef(Table buf, int Ingredient)//pass the table and the ingredient that the chef has infinite of as parameters when initializing the objects
    {
        buffer = buf;
        ing = Ingredient;
    }

    public void run()
    {
        int item = 0;

        for(int i = 0; i < 20; i++)
        {
            if(buffer.CheckTable()==-1)break;
            if(ing + ingredient[buffer.CheckTable()] == 7) { //If the ingredient the chef has infinite of matches the combination on the table, make the sandwich and eat it
                item = buffer.removeFirst();
                System.out.println(Thread.currentThread().getName() + " consumed " + ingname[item]);//Output string
            }
            try {Thread.sleep(1500);}
            catch (InterruptedException e) {}
        }
    }
}