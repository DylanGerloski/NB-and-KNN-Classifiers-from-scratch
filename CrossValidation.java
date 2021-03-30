import java.util.ArrayList;
import java.util.Scanner;

public class CrossValidation{

  private ArrayList<Folds> folds;
  private ArrayList<Double> accuracy;
  private String classifier;



  public CrossValidation(ArrayList<Folds> folds, String classifier){
    this.folds = folds;
    this.accuracy = new ArrayList<>();
    this.classifier = classifier;
  }



  public void runValidaiton(){
    int k = 0;
    if(classifier.equals("KNN")){
      System.out.println("Enter your values of K");
      Scanner scan = new Scanner(System.in);
      k = scan.nextInt();
    }
    for(int i = 0; i < folds.size(); i++){
      ArrayList<Data> testData = new ArrayList<>();
      ArrayList<Data> trainingData = new ArrayList<>();
      for(int j = 0; j < folds.size(); j++){
        if(j != i){
          trainingData.addAll(folds.get(j).getData());
        }else{
          testData.addAll(folds.get(j).getData());
        }
      }
      if(classifier.equals("NB")){
        NB nb = new NB(trainingData, testData);
        ArrayList<String> classes = nb.runNb();
        calcualteAccuracy(classes, testData);
      }
      else if(classifier.equals("KNN")){
        KNN knn = new KNN(trainingData, testData, k);
        ArrayList<String> classes = knn.runKNN();
        calcualteAccuracy(classes, testData);
      }
      else {
        System.out.println("You did not enter NB or KNN");
      }
    }
    int total = 0;
    Double averageAccuracy = 0.0;
   for(Double d : accuracy){
      averageAccuracy += d;
      total++;
    }
    averageAccuracy = averageAccuracy / total;
    System.out.println("Accuracy = " + averageAccuracy);

  }


  private void calcualteAccuracy(ArrayList<String> calcualtedClasses, ArrayList<Data> testData){
    Double correct = 0.0;
    Double total = 0.0;
    for(int i = 0; i < calcualtedClasses.size(); i ++){
      if(testData.get(i).getclass().equals(calcualtedClasses.get(i))){
        correct ++;
          }
      total++;
    }
    accuracy.add(correct/total);
    }
}
