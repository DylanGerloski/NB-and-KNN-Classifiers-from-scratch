public class Result implements Comparable<Result> {
  double distance;
  String Class;
  public Result(double distance, String Class){
    this.distance = distance;
    this.Class = Class;
  }

  public Double getDistance(){return distance;}

  public String getclass(){return Class;}

  public int compareTo(Result anotherResult){
    if(this.getDistance() < anotherResult.getDistance()){
      return -1;
    }else if(this.getDistance() > anotherResult.getDistance()){
      return 1;
    }else{
      return 0;
    }
  }
}
