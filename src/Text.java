import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Text
extends Thread {
    int score = 0;
    Integer highScore = null;
    private final String HIGHSCORE_FILE = "files/scores.dat";
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;
    String str1;
    String str2;
    String bigText;
    String scoreStr;
    int delay1 = 0;
    int delay2 = 0;
    int delay3 = 0;
    Color c1;
    Color c2;
    Color Bc;
    int tempScore;
    boolean isSrt1Score;
    boolean showScore = false;
    Point3D magozPoint;
    Font bigTextFont;
    Font scoreFont;

    public Text(Point3D magozPoint) {
        this.magozPoint = magozPoint;
        this.str1 = null;
        this.str2 = null;
        this.bigText = null;
        this.Bc = Color.orange;
        try {
            this.bigTextFont = Font.createFont(0, new FileInputStream("files/fonts/door.ttf")).deriveFont(0, 40.0f);
            this.scoreFont = Font.createFont(0, new FileInputStream("files/fonts/Playbill.ttf")).deriveFont(0, 50.0f);
        }
        catch (Exception e) {
            System.out.println("font not found");
        }
        this.loadScoreFile();
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            this.sleepy(100);
            this.delay1 -= 100;
            this.delay2 -= 100;
            this.delay3 -= 100;
        }
    }

    public void addText(String text, Color color) {
        this.str2 = this.str1;
        this.delay2 = this.delay1;
        this.c2 = this.c1;
        this.str1 = text;
        this.delay1 = 2000;
        this.c1 = color;
        this.isSrt1Score = false;
    }

    public void addScore(int a) {
        this.score += a;
        if (this.isSrt1Score) {
            this.tempScore += a;
            this.str1 = "+" + this.tempScore;
            this.delay1 = 2000;
            this.c1 = Color.orange;
        } else {
            this.str2 = this.str1;
            this.delay2 = this.delay1;
            this.c2 = this.c1;
            this.tempScore = a;
            this.str1 = "+" + this.tempScore;
            this.delay1 = 2000;
            this.c1 = Color.orange;
        }
        this.isSrt1Score = true;
    }

    public void addBigText(String text) {
        this.bigText = text;
        this.delay3 = 5000;
    }

    public void showScore(int score) {
        this.scoreStr = "" + score;
        this.showScore = true;
    }

    public void stopShowingScore() {
        this.showScore = false;
    }

    public void show(Graphics page) {
        if (this.delay1 > 0) {
            page.setFont(new Font("Arial", 0, 15));
            page.setColor(this.c1);
            page.drawString(this.str1, (int)this.magozPoint.x, (int)(this.magozPoint.y - 125.0));
        } else {
            this.tempScore = 0;
        }
        if (this.delay2 > 0) {
            page.setFont(new Font("Arial", 0, 15));
            page.setColor(this.c2);
            page.drawString(this.str2, (int)this.magozPoint.x, (int)(this.magozPoint.y - 140.0));
        }
        if (this.delay3 > 0) {
            page.setFont(this.bigTextFont);
            page.setColor(this.Bc);
            page.drawString(this.bigText, (int)this.magozPoint.x - 150, (int)(this.magozPoint.y - 200.0));
        }
        if (this.showScore) {
            page.setFont(new Font("Arial", 0, 40));
            page.setColor(this.Bc);
            page.drawString("Score: " + this.scoreStr + ".", (int)this.magozPoint.x - 150, (int)this.magozPoint.y);
        }
        page.setFont(this.scoreFont);
        page.setColor(Color.orange);
        page.drawString(String.valueOf(this.score), 5, 35);
    }

    public void setHighScore() {
        this.loadScoreFile();
        if (this.score > this.highScore) {
            this.highScore = this.score;
            this.updateScoreFile();
        }
    }

    public int getHighScore() {
        this.loadScoreFile();
        return this.highScore;
    }

    public void restartScore() {
        this.highScore = 0;
        this.updateScoreFile();
    }

    public void loadScoreFile() {
        try {
            try {
                this.inputStream = new ObjectInputStream(new FileInputStream("files/scores.dat"));
                this.highScore = (Integer)this.inputStream.readObject();
            }
            catch (FileNotFoundException e) {
                System.out.println("[Laad] FNF Error: " + e.getMessage());
                try {
                    if (this.outputStream != null) {
                        this.outputStream.flush();
                        this.outputStream.close();
                    }
                }
                catch (IOException e2) {
                    System.out.println("[Laad] IO Error: " + e2.getMessage());
                }
            }
            catch (Exception e) {
                System.out.println("[Laad] IO Error: " + e.getMessage());
                try {
                    if (this.outputStream != null) {
                        this.outputStream.flush();
                        this.outputStream.close();
                    }
                }
                catch (IOException e3) {
                    System.out.println("[Laad] IO Error: " + e3.getMessage());
                }
            }
        }
        finally {
            try {
                if (this.outputStream != null) {
                    this.outputStream.flush();
                    this.outputStream.close();
                }
            }
            catch (IOException e) {
                System.out.println("[Laad] IO Error: " + e.getMessage());
            }
        }
    }

    public void updateScoreFile() {
        try {
            try {
                this.outputStream = new ObjectOutputStream(new FileOutputStream("files/scores.dat"));
                this.outputStream.writeObject(this.highScore);
            }
            catch (FileNotFoundException e) {
                System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
                try {
                    if (this.outputStream != null) {
                        this.outputStream.flush();
                        this.outputStream.close();
                    }
                }
                catch (IOException e2) {
                    System.out.println("[Update] Error: " + e2.getMessage());
                }
            }
            catch (IOException e) {
                System.out.println("[Update] IO Error: " + e.getMessage());
                try {
                    if (this.outputStream != null) {
                        this.outputStream.flush();
                        this.outputStream.close();
                    }
                }
                catch (IOException e3) {
                    System.out.println("[Update] Error: " + e3.getMessage());
                }
            }
        }
        finally {
            try {
                if (this.outputStream != null) {
                    this.outputStream.flush();
                    this.outputStream.close();
                }
            }
            catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }

    private void sleepy(int wait) {
        try {
            Text.sleep(wait);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
