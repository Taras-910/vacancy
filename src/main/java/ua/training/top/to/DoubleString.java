package ua.training.top.to;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DoubleString implements Serializable {

    @NotNull
    private String languageTask;
    @NotNull
    private String workplaceTask;

    public DoubleString(String languageTask, String workplaceTask) {
        this.languageTask = languageTask.toLowerCase();
        this.workplaceTask = workplaceTask.toLowerCase();
    }

    public DoubleString(){}

    public String getLanguageTask() {
        return languageTask;
    }

    public void setLanguageTask(String languageTask) {
        this.languageTask = languageTask.toLowerCase();
    }

    public String getWorkplaceTask() {
        return workplaceTask;
    }

    public void setWorkplaceTask(String workplaceTask) {
        this.workplaceTask = workplaceTask.toLowerCase();
    }

    @Override
    public String toString() {
        return "DoubleString{" +
                "languageTask='" + languageTask + '\'' +
                ", workplaceTask='" + workplaceTask + '\'' +
                '}';
    }
}