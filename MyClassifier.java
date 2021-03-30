import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.*;

public class MyClassifier{
  public static void main(String[] args){
  /*  String trainingFile = args[0];
    String testFile = args[1];
    String choice = args[2]; */
    System.out.println("Enter Yes if you'd like to make folds from a csv, otherwise type no.");
    Scanner sc = new Scanner(System.in);
    String choice = sc.nextLine().toUpperCase();
    if(choice.equals("YES")){
    sort();
    }
    System.out.println("Enter KNN for K nearest Neighbor or NB for Naive Bayes.");
    Scanner scan = new Scanner(System.in);
    String classifier = scan.nextLine();
    System.out.println("Enter a fold file to run cross validation on to test the classifiers accuracy or you can just enter folds.csv");
    String fi = scan.nextLine();
    CrossValidation cv = new CrossValidation(createFolds(fi), classifier); //Run this for acuracy test
    cv.runValidaiton();

    /*If youd like to use your own data set, uncomment the parts below and above.
      Note that the program will only work on data sets with numeric values and classes of Yes or No

    */
   /* if(choice.equals("NB")){
      ArrayList<ArrayList<Double>> trainingAttributes = new ArrayList<>();
      ArrayList<String> Class = new ArrayList<>();
      trainingAttributes.addAll(storeValues(trainingFile));
      Class.addAll(storeClass(trainingFile));
      ArrayList<Data> data = new ArrayList<>();
      for(int i = 0 ; i < trainingAttributes.size(); i ++){
        data.add(new Data(trainingAttributes.get(i), Class.get(i)));
      }
      ArrayList<ArrayList<Double>> testingAttributes = new ArrayList<>();
      testingAttributes.addAll(storeValues(testFile));
      ArrayList<Data> testData = new ArrayList<>();
      for(int j = 0 ; j < testingAttributes.size(); j ++){
        testData.add(new Data(testingAttributes.get(j)));
      }
      NB nb = new NB(data, testData);
      ArrayList<String> classes = nb.runNb();

    }else{
      char c = choice.charAt(0);
      int k = Character.getNumericValue(c);
      ArrayList<ArrayList<Double>> trainingAttributes = new ArrayList<>();
      ArrayList<String> Class = new ArrayList<>();
      trainingAttributes.addAll(storeValues(trainingFile));
      Class.addAll(storeClass(trainingFile));
      ArrayList<Data> data = new ArrayList<>();
      for(int i = 0 ; i < trainingAttributes.size(); i ++){
        data.add(new Data(trainingAttributes.get(i), Class.get(i)));
      }
      ArrayList<ArrayList<Double>> testingAttributes = new ArrayList<>();
      testingAttributes.addAll(storeValues(testFile));
      ArrayList<Data> testData = new ArrayList<>();
      for(int j = 0 ; j < testingAttributes.size(); j ++){
        testData.add(new Data(testingAttributes.get(j)));
      }
      KNN knn = new KNN(data, testData, k);
      knn.runKNN();
    }
*/
  }


  public static void sort(){
    System.out.println("Enter a CSV file you wish to create folds for");
    Scanner scan = new Scanner(System.in);
    String file = scan.nextLine();
    ArrayList<ArrayList<Double>> trainingAttributes = new ArrayList<>();
    ArrayList<String> Class = new ArrayList<>();
    trainingAttributes.addAll(storeValues(file));
    Class.addAll(storeClass(file));
    ArrayList<Data> dataList = new ArrayList<>();

    for(int i = 0 ; i < trainingAttributes.size(); i ++){
      dataList.add(new Data(trainingAttributes.get(i), Class.get(i)));
    }

    Collections.shuffle(dataList);

     try(BufferedWriter bf = new BufferedWriter(new FileWriter("folds.csv"))){
      int foldSize = dataList.size() / 10;
      int count = 0;
      int foldNum = 1;
      bf.write("fold1");
      bf.newLine();
      for(Data data : dataList){
        count ++;
        String str = data.getDA() + "," + data.getclass();
        str = str.replaceAll("\\[","").replaceAll("\\]", "").replaceAll(" ","");
        String space = "\n";
        bf.write(str);
        bf.newLine();
        if(count % foldSize == 0){
          bf.newLine();
          bf.write("fold" + (foldNum));
          foldNum ++;
          bf.newLine();
          
        }
      }
        bf.close();
    }
      catch(IOException e){

      }

  }


  public static ArrayList<ArrayList<Double>> storeValues(String fileName){
    File file = new File(fileName);
    try {
      Scanner scanner = new Scanner(file);
      ArrayList<ArrayList<Double>> data = new ArrayList<>();
      while(scanner.hasNextLine()){
          String line = scanner.nextLine();
          if(line.equals("")){
            break;
          }else if(line.charAt(0) == 'f'){
            continue;
          }else{
            String nline;
            nline = line.replace("no" , "" );
            nline = nline.replace("yes" , "");
            String[] Line = nline.split(",");
            ArrayList<Double> dub = new ArrayList <>();
            for(String string : Line){
              Double d = Double.parseDouble(string);
              dub.add(d);
            }
            data.add(dub);
        }
      }
     return data;
    }catch(FileNotFoundException e){
      e.printStackTrace();
      return null;
    }
  }

  public static ArrayList<String> storeClass(String fileName){
    File file = new File(fileName);
    try{
      Scanner scanner = new Scanner(file);
      ArrayList<String> classes = new ArrayList<>();
      while(scanner.hasNextLine()){
        String line = scanner.nextLine();
        if(line.equals("")){
          break;
        }else if(line.charAt(0) == 'f'){
          continue;
        }else{
          String nLine = line.replaceAll("\\d", "");
          nLine = nLine.replaceAll(",", "");
          nLine = nLine.replaceAll("\\.", "");
          classes.add(nLine);
        }
      }
    return classes;
    }catch(FileNotFoundException e){
      e.printStackTrace();
      return null;
    }
  }

  public static ArrayList<Folds> createFolds(String fileName){
      File file = new File(fileName);
      ArrayList<Folds> folds = new ArrayList<>();
      try{
        Scanner scanner = new Scanner(file);
        ArrayList<Data> dataList = new ArrayList<>();
        while(scanner.hasNextLine()){
          String line = scanner.nextLine();
          if(line.equals("")){
            ArrayList<Data> d = new ArrayList<>();
            d.addAll(dataList);
            Folds fold = new Folds(d);
            dataList.clear();
            folds.add(fold);
            continue;
          }
          if(line.charAt(0) == 'f'){
            continue;
          }else{
            String d = line.replaceAll("yes", "").replaceAll("no", "");
            String[] dd = d.split(",");
            ArrayList<Double> dub = new ArrayList <>();
            for(String string : dd){
              Double ddd = Double.parseDouble(string);
              dub.add(ddd);
            }
            String Class = line.replaceAll("\\d", "").replaceAll(",", "").replaceAll("\\.", "");
            Data data = new Data(dub, Class);
            dataList.add(data);
        }


      }
      ArrayList<Data> d = new ArrayList<>();
      d.addAll(dataList);
      Folds fold = new Folds(d);
      dataList.clear();
      folds.add(fold);
      return folds;
    }
      catch(FileNotFoundException e){
        e.printStackTrace();
        return null;
      }
  }



}
