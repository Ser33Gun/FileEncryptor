import net.lingala.zip4j.core.ZipFile;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DecrypterThread extends Thread
{
    private GUIForm form;
    private File file;
    private String password;

    public DecrypterThread(GUIForm form)
    {
        this.form = form;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public void run()
    {
        onStart();
        try
        {
            String outPath = getOutputPath();
            ZipFile zipFile = new ZipFile(file);
            zipFile.setPassword(password);
            LocalTime startTime = LocalTime.now();
            zipFile.extractAll(outPath);
            LocalTime endTime = LocalTime.now();
            Duration duration = Duration.between(startTime, endTime);
            form.showFinished(Math.abs(duration.toSeconds()));
        }
        catch (Exception ex) {
            // Меняем вывод при неправильном пароле.
            form.showWarning("Пароль указан не верно!");
        }
        form.setButtonsEnabled(true);
    }

    private void onStart()
    {
        form.setButtonsEnabled(false);
    }

    private String getOutputPath()
    {
        String path = file.getAbsolutePath()
                .replaceAll("\\.enc$", "");
        for(int i = 1; ; i++)
        {
            String number = i > 1 ? Integer.toString(i) : "";
            String outPath = path + number;
            if(!new File(outPath).exists()) {
                return outPath;
            }
        }
    }
}
