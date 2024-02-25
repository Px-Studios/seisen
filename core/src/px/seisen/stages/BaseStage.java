package px.seisen.stages;

public class BaseStage {
    public double startTimestamp;
    public String stageName, stageId;

    public BaseStage(String stageName, String stageId) {
        this.startTimestamp = System.currentTimeMillis();
        this.stageName = stageName;
        this.stageId = stageId;
    }

    public double getElapsedTime() {
        return System.currentTimeMillis() - this.startTimestamp;
    }
}
