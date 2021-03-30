import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;

public class KNN{
  private ArrayList<Data> trainingDataList;
  private ArrayList<Data> testDataList;
  private int k;
  private ArrayList<Result> resultList;

  public KNN(ArrayList<Data> trainingDataList,ArrayList<Data> testDataList , int k){
    this.trainingDataList = trainingDataList;
    this.testDataList = testDataList;
    this.k = k;
    resultList = new ArrayList<>();
  }



  public ArrayList<String> runKNN(){
    ArrayList<String> result = new ArrayList<>();
    for(Data testData : testDataList){
      getDistance(testData);
      result.add(findMajorityClass(findKNearest()));
    }
    return result;
  }
  private void getDistance(Data testData){
    for(Data TD : trainingDataList){
      double dist = 0.0;
      for(int i = 0; i <TD.getDA().size(); i++){
        dist += (Math.pow(TD.getDA().get(i) - testData.getDA().get(i), 2));
      }
      double distance = Math.sqrt(dist);
      resultList.add(new Result(distance, TD.getclass()));
    }
  }

  private String[] findKNearest(){
    Collections.sort(resultList);
    String[] classes = new String[k];
    for(int i = 0; i < k; i++){
      classes[i] = resultList.get(i).getclass();
    }
    return classes;
  }

  private String findMajorityClass(String[] array){
      resultList.clear();
      int yesCount = 0;
      int noCount = 0;
      for(int i = 0; i < array.length ; i ++){
        if(array[i].equals("yes")){
          yesCount++;
        }
        if(array[i].equals("no")){
          noCount++;
        }
      }
      if(yesCount > noCount || yesCount == noCount){
          //System.out.println("yes");
          return "yes";

      }else{
          //System.out.println("no");
          return "no";
      }
  }

}
