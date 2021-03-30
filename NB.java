import java.lang.Math;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
public class NB{
  private ArrayList<Data> trainingDataList;
  private ArrayList<Data> testDataList;
  private ArrayList<Attributes> statisticsList;


  public NB(ArrayList<Data> trainingDataList, ArrayList<Data> testDataList){
    this.trainingDataList = trainingDataList;
    this.testDataList = testDataList;
    this.statisticsList = new ArrayList<>();
  }


  public class Attributes{
    private Double yesMean;
    private Double noMean;
    private Double yesSTD;
    private Double noSTD;
    private double totalYes;
    private double totalNo;

    public Attributes(Double yesMean, Double noMean, Double yesSTD, Double noSTD){
      this.yesMean = yesMean;
      this.noMean = noMean;
      this.yesSTD = yesSTD;
      this.noSTD = noSTD;
    }


    public Double getYesMean(){return yesMean;}
    public Double getNoMean(){return noMean;}
    public Double getYesSTD(){return yesSTD;}
    public Double getNoSTD(){return noSTD;}
  }

  public ArrayList<String> runNb(){
    ArrayList<String> result = new ArrayList<>();
    double yesCount = 0.0;
    double noCount = 0.0;
    for(Data data : trainingDataList){
      if(data.getclass().equals("yes")){
        yesCount ++;
      }else{
        noCount ++;
      }

    }
    double yesclassP = yesCount / (yesCount + noCount);
    double noclassP = noCount / (yesCount + noCount);
    for(Data data : testDataList){
        calculateMean();
        String Class = calculateNB(data, yesclassP, noclassP);
        result.add(Class);
    }
    return result;

  }



  private String calculateNB(Data data, Double yesP, Double noP){
    Double yes = 1.0;
    Double no = 1.0;
    Double yesTotal = 0.0;
    Double noTotal= 0.0;
    for(int i = 0; i < data.getDA().size(); i ++){
      Double desnityFunctionYes = 0.0;
      if(statisticsList.get(i).getYesSTD() == Double.POSITIVE_INFINITY){
        desnityFunctionYes = 1.0;
      }else{
        desnityFunctionYes =  Math.pow(data.getDA().get(i) - statisticsList.get(i).getYesMean(), 2);
        desnityFunctionYes = - (desnityFunctionYes / (2 * Math.pow(statisticsList.get(i).getYesSTD(), 2)));
        desnityFunctionYes = Math.pow(Math.E, desnityFunctionYes);
        desnityFunctionYes = desnityFunctionYes * (1 / (Math.sqrt(2 * Math.PI) * statisticsList.get(i).getYesSTD()));
      }
      yes *= desnityFunctionYes;
      Double desnityFunctionNo = 0.0;
      if(statisticsList.get(i).getNoSTD() == Double.POSITIVE_INFINITY){
        desnityFunctionNo = 1.0;
      }else{
        desnityFunctionNo =  Math.pow(data.getDA().get(i) - statisticsList.get(i).getNoMean(), 2);
        desnityFunctionNo = - (desnityFunctionNo / (2 * Math.pow(statisticsList.get(i).getNoSTD(), 2)));
        desnityFunctionNo = Math.pow(Math.E, desnityFunctionNo);
        desnityFunctionNo = desnityFunctionNo * (1 / (Math.sqrt(2 * Math.PI) * statisticsList.get(i).getNoSTD()));
      //  System.out.println("No PDF: " + desnityFunctionNo);
      }
    //  System.out.println("No PDF: " + desnityFunctionNo);

      no *= desnityFunctionNo;
    }
  //  System.out.println("no Total:" + no + " " + " yes Total: " + yes);
    if(statisticsList.get(0).getYesSTD() == Double.POSITIVE_INFINITY){
      yes = 1.0;
    }else{
    yes *= yesP;}
    if(statisticsList.get(0).getNoSTD() == Double.POSITIVE_INFINITY){
    no = 1.0;
  }else{
    no *= noP;
    }
    if(yes >= no){
      //System.out.println("yes");
      statisticsList.clear();
      return "yes";
    }else{
      //System.out.println("no");
      statisticsList.clear();
      return "no";
    }

  }



  private void calculateMean(){
    for(int i = 0; i < trainingDataList.get(0).getDA().size(); i ++){
      Double yesTotal = 0.0;
      Double noTotal = 0.0;
      Double noMean = 0.0;
      Double yesMean = 0.0;
      int index = 0;
      Double yesCount = 0.0;
      Double noCount = 0.0;
      index = i;
      for(int j = 0; j < trainingDataList.size(); j ++){
        if(trainingDataList.get(j).getclass().equals("yes")){
          yesTotal += trainingDataList.get(j).getDA().get(i);
          yesCount ++;
        }else{
          noTotal += trainingDataList.get(j).getDA().get(i);
          noCount ++;
        }
      }
    //  System.out.println(noTotal + " " );
      noMean = noTotal / noCount;
      yesMean = yesTotal / yesCount;


      CalcualteSTD(yesMean, noMean, yesCount, noCount, index);
    }
  }

  private void CalcualteSTD(Double yesMean, Double noMean, Double yesCount, Double noCount,
  int index){
    Double stdYes = 0.0;
    Double stdNo = 0.0;
    for(int j = 0; j < trainingDataList.size(); j ++){
        if(trainingDataList.get(j).getclass().equals("yes")){
          stdYes += Math.pow(trainingDataList.get(j).getDA().get(index) - yesMean, 2);
        }else{
          stdNo += Math.pow(trainingDataList.get(j).getDA().get(index) - noMean, 2);
        }
    }
    stdYes = Math.sqrt(stdYes / (yesCount - 1) );
    stdNo = Math.sqrt(stdNo / (noCount - 1) );
    //System.out.println("STD yes: " + stdYes + " MEAN YES: " + yesMean + " STD NO: "+ stdNo + " MEAN NO " + noMean);
    statisticsList.add(new Attributes(yesMean, noMean, stdYes, stdNo));

  }





}
