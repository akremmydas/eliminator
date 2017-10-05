import javax.microedition.lcdui.*;
public class GenericScreen extends Canvas {
	// Fonts
	static final Font LargeItalic = Font.getFont (Font.FACE_MONOSPACE,Font.STYLE_ITALIC, Font.SIZE_LARGE);
	static final Font SmallItalic = Font.getFont (Font.FACE_MONOSPACE,Font.STYLE_ITALIC, Font.SIZE_SMALL);
	static final Font LargeBold = Font.getFont (Font.FACE_MONOSPACE,Font.STYLE_BOLD, Font.SIZE_LARGE);
	static final Font SmallBold = Font.getFont (Font.FACE_MONOSPACE,Font.STYLE_BOLD, Font.SIZE_SMALL);
	// Colors
	static final int White = 0x00ffffff;
	static final int Black = 0x00000000;
	static final int DarkRed = 0x00961e1e;
	// BackGround Image
	public Image BackGroundImage;
	// Constructor
	public GenericScreen() {
		setFullScreenMode(true);
		try {
			BackGroundImage = Image.createImage("/BackGroundImage.jpg");
		} catch(Exception ex) {System.out.println("::BackGroundImage::" + ex);}
	}
	// RUN
	public void run() {while(true) {repaint();}}
	// Paint Method
	public void paint(Graphics g) {}
	// Beep method
	public void beep(){
		try {
			javax.microedition.media.Manager.playTone(90, 40,100);
		} catch (Exception ex) {System.out.println("::Beep::" + ex);}		
	}
}
