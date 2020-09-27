import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;
import java.time.Duration;
import java.time.LocalTime;

public class EncrypterThread extends Thread
{
    private GUIForm form;
    private File file;
    private ZipParameters parameters;

    public EncrypterThread(GUIForm form)
    {
        this.form = form;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public void setPassword(String password)
    {
        parameters = ParametersContainer.getParameters();
        parameters.setPassword(password);
    }

    @Override
    public void run()
    {
        onStart();
        try
        {
            String archiveName = getArchiveName();
            ZipFile zipFile = new ZipFile(archiveName);
            if(file.isDirectory()) {
                LocalTime startTime = LocalTime.now();
                zipFile.addFolder(file, parameters);
                LocalTime endTime = LocalTime.now();
                Duration duration = Duration.between(startTime, endTime);
                onFinish(Math.abs(duration.toSeconds()));
            }
        }
        catch (Exception ex) {
            form.showWarning(ex.getMessage());
        }
        form.setButtonsEnabled(true);

    }

    private void onStart()
    {
        form.setButtonsEnabled(false);
    }

    private void onFinish(long time)
    {
        parameters.setPassword("");
        form.setButtonsEnabled(true);
        form.showFinished(time);
    }

    private String getArchiveName()
    {
        for(int i = 1; ; i++)
        {
            String number = i > 1 ? Integer.toString(i) : "";
            String archiveName = file.getAbsolutePath() +
                    number + ".enc";
            if(!new File(archiveName).exists()) {
                return archiveName;
            }
        }
    }
}
