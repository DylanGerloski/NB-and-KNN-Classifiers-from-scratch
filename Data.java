import java.util.ArrayList;

public  class Data{
    private ArrayList<Double> dataAttributes;
    private String Class;


    public Data(ArrayList<Double> dataAttributes, String Class){
      this.dataAttributes = dataAttributes;
      this.Class = Class;
    }

    public Data(ArrayList<Double> dataAttributes){
      this.dataAttributes = dataAttributes;
      this.Class = null;
    }
    public ArrayList<Double> getDA(){
      return dataAttributes;
    }
    public String getclass(){
      return Class;
    }


}
