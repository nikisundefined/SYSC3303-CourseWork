/**
 * Consumer is the class for the consumer thread. CHEFS
 */
class Consumer implements Runnable
{
    private BoundedBuffer buffer;
    private int ing;
    int[] ingredient = {6,5,3};
    String[] ingname = {"Peanut Butter and Bread","Bread and Jam","Peanut Butter and Jam"};
    public Consumer(BoundedBuffer buf, int Ingredient)
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
            if(ing + ingredient[buffer.CheckTable()] == 7) {
                item = buffer.removeFirst();
                System.out.println(Thread.currentThread().getName() + " consumed " + ingname[item]);
            }
            try {Thread.sleep(1500);}
            catch (InterruptedException e) {}
        }
    }
}