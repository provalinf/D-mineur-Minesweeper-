package demineur.model;

import demineur.Main;
import demineur.view.View;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Valentin.
 */
public class Data {

	public static final int HEIGHT_MAX_BUTT_TOOLBAR = 36;
	public static final int NB_FLAG_MAX_SUPPL_AUTORISE = 3;

	private Mine[][] platJeu;
	private boolean jeuPerdu;
	private PlateauSize dimension;
	private int tauxMine;

	private ArrayList<View> viewList;
	private Map<String, ImageIcon> jeuImg;
	private Map<String, ImageIcon> toolbarImg;
	private ImageIcon icon;
	private Map<String, URL> jeuSound;
	private Main mainJFrame;

	public Data(Main mainJFrame) {
		this.mainJFrame = mainJFrame;
		viewList = new ArrayList<>();
		dimension = new PlateauSize(10, 10);
		tauxMine = 10;
		jeuImg = new HashMap<>(3);
		toolbarImg = new HashMap<>(3);
		jeuSound = new HashMap<>(3);
		loadRess();
		initAndReset();
	}

	@SuppressWarnings("WeakerAccess")
	public void initAndReset() {
		jeuPerdu = false;
		platJeu = new Mine[dimension.getX()][dimension.getY()];
		initPlateau();
		generateJeu();
	}

	public void resetSamePart() {
		jeuPerdu = false;
		hideAndDeflagAllMine();
	}

	private void loadRess() {
		jeuImg.put("flag", new ImageIcon(getClass().getResource("/demineur/img/flag.png")));
		jeuImg.put("goodflag", new ImageIcon(getClass().getResource("/demineur/img/goodflag.png")));
		jeuImg.put("mine", new ImageIcon(getClass().getResource("/demineur/img/minibomb.png")));
		jeuImg.put("minexpl", new ImageIcon(getClass().getResource("/demineur/img/minibombexp.png")));
		resizeImg(jeuImg, 16);

		toolbarImg.put("mine", new ImageIcon(getClass().getResource("/demineur/img/minibomb.png")));
		toolbarImg.put("new", new ImageIcon(getClass().getResource("/demineur/img/new.png")));
		toolbarImg.put("quit", new ImageIcon(getClass().getResource("/demineur/img/quit.png")));
		resizeImg(toolbarImg, 22);

		icon = new ImageIcon(getClass().getResource("/demineur/img/icon.png"));

		jeuSound.put("discov", getClass().getResource("/demineur/sound/discov.wav"));
		jeuSound.put("demarrage", getClass().getResource("/demineur/sound/lancement.wav"));
		jeuSound.put("minexpl", getClass().getResource("/demineur/sound/mine3.wav"));
		jeuSound.put("finish", getClass().getResource("/demineur/sound/finish.wav"));
	}

	private void initPlateau() {
		for (int i = 0; i < dimension.getX(); i++) {
			for (int j = 0; j < dimension.getY(); j++) {
				platJeu[i][j] = new Mine();
			}
		}
	}

	private void generateJeu() {
		generateMine();
		for (int x = 0; x < platJeu.length; x++) {
			for (int y = 0; y < platJeu[x].length; y++) {
				int nbMines = 0;

				if (x - 1 >= 0) {
					if (y - 1 >= 0 && platJeu[x - 1][y - 1].isMine()) nbMines++;
					if (platJeu[x - 1][y].isMine()) nbMines++;
					if (y + 1 < dimension.getY() && platJeu[x - 1][y + 1].isMine()) nbMines++;
				}
				if (y - 1 >= 0 && platJeu[x][y - 1].isMine()) nbMines++;
				if (y + 1 < dimension.getY() && platJeu[x][y + 1].isMine()) nbMines++;
				if (x + 1 < dimension.getX()) {
					if (y - 1 >= 0 && platJeu[x + 1][y - 1].isMine()) nbMines++;
					if (platJeu[x + 1][y].isMine()) nbMines++;
					if (y + 1 < dimension.getY() && platJeu[x + 1][y + 1].isMine()) nbMines++;
				}
				platJeu[x][y].setNbMineProxim(nbMines);
			}
		}
	}

	private void hideAndDeflagAllMine() {
		for (Mine[] aPlatJeu : platJeu) {
			for (Mine anAPlatJeu : aPlatJeu) {
				anAPlatJeu.setFlag(false);
				anAPlatJeu.setHide(true);
			}
		}
	}

	private void resizeImg(Map<String, ImageIcon> hasmap, int taille) {
		for (Map.Entry<String, ImageIcon> img : hasmap.entrySet()) {
			Image imgResiz = img.getValue().getImage().getScaledInstance(taille, taille, Image.SCALE_AREA_AVERAGING);
			img.setValue(new ImageIcon(imgResiz));
		}
	}

	private void generateMine() {
		for (int i = 0; i < getNbMine(); i++) {
			int x, y;
			do {
				int empl = ThreadLocalRandom.current().nextInt(0, dimension.getX() * dimension.getY());
				y = empl / dimension.getX();
				x = empl - y * dimension.getX();
				//System.out.println("(Ré)génération");
			} while (platJeu[x][y].isMine());	/*S'il s'agit déjà d'une mine, régénération*/

			platJeu[x][y].setMine(true);
			//System.out.println(x + ", " + y);
		}
	}

	public void unHideAllBomb() {
		for (Mine[] x : platJeu)
			for (Mine xy : x)
				if (xy.isMine() && xy.isHide()) xy.setHide(false);
	}

	public boolean verifJeuGagne() {
		return !jeuPerdu && /*verifAllBombFlag() &&*/ verifAllNoBombDiscov();
	}

	/**
	 * Vérifie si toutes les cases qui ne sont pas des mines
	 * ont été découverte
	 *
	 * @return boolean : true = toutes les cases découvertes, false = inversement
	 */
	private boolean verifAllNoBombDiscov() {
		/*for (Mine[] x : platJeu)		// À modifier, ne convient pas aux profs
			for (Mine xy : x)
				if (!xy.isMine() && xy.isHide()) return false;
		return true;*/

		int x = 0, y;
		boolean allNoBombDiscov = true;
		while (x < dimension.getX() && allNoBombDiscov) {
			y = 0;
			while (y < dimension.getY() && allNoBombDiscov) {
				if (!platJeu[x][y].isMine() && platJeu[x][y].isHide()) allNoBombDiscov = false;
				y++;
			}
			x++;
		}
		return allNoBombDiscov;
	}

	/**
	 * Vérifie si toutes les mines ont étés marquées.
	 *
	 * @return boolean : true = toutes marquées, false = inversement
	 */
	@SuppressWarnings("unused")
	private boolean verifAllBombFlag() {
		/*for (Mine[] x : platJeu)		// À modifier, ne convient pas aux profs
			for (Mine xy : x)
				if (xy.isMine() && !xy.isFlag()) return false;
		return true;*/

		int x = 0, y;
		boolean allBombFlag = true;
		while (x < dimension.getX() && allBombFlag) {
			y = 0;
			while (y < dimension.getY() && allBombFlag) {
				if (platJeu[x][y].isMine() && !platJeu[x][y].isFlag()) allBombFlag = false;
				y++;
			}
			x++;
		}
		return allBombFlag;
	}

	public void playSound(String key) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(jeuSound.get(key));
			clip.open(inputStream);
			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		}
	}

	public void autoResizeFrame() {
		mainJFrame.pack();
		mainJFrame.setLocationRelativeTo(null);
	}

	/* --------- */

	@SuppressWarnings("unused")
	public URL getJeuSound(String key) {
		return jeuSound.get(key);
	}

	@SuppressWarnings("SameParameterValue")
	public int getMillisecSound(String key) {
		int duratMillisec = 0;
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(jeuSound.get(key));
			long frames = inputStream.getFrameLength();
			AudioFormat format = inputStream.getFormat();
			duratMillisec = (int) ((int) (frames + 0.0) / format.getFrameRate() * 1000);
		} catch (IOException | UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		}
		return duratMillisec;
	}

	public int getNbMine() {
		return (int) (dimension.getX() * dimension.getY() * (tauxMine / 100.));
	}

	public int[] getNbFlagAndGoodFlag() {
		int count = 0, good = 0;
		for (Mine[] x : platJeu)
			for (Mine xy : x)
				if (xy.isFlag()) {
					count++;
					if (xy.isMine()) good++;
				}
		return new int[]{count, good};
	}

	public int getTauxMine() {
		return tauxMine;
	}

	public ImageIcon getToolbarImg(String key) {
		return toolbarImg.get(key);
	}

	public ImageIcon getJeuImg(String key) {
		return jeuImg.get(key);
	}

	public Image getIconApp() {
		return icon.getImage();
	}

	public boolean isJeuPerdu() {
		return jeuPerdu;
	}

	public Mine getMineXY(int x, int y) {
		return platJeu[x][y];
	}

	public PlateauSize getDimension() {
		return dimension;
	}

	@SuppressWarnings("WeakerAccess")
	public ArrayList<View> getViewList() {
		return viewList;
	}

	public View getViewId(int id) {
		return viewList.get(id);
	}

	/* --------- */

	public void setDimension(PlateauSize dimension) {
		this.dimension.setX(dimension.getX());
		this.dimension.setY(dimension.getY());
	}

	public void setTauxMine(int tauxMine) {
		this.tauxMine = tauxMine;
	}

	public void setJeuPerdu() {
		this.jeuPerdu = true;
	}

	public void addToViewList(View view) {
		viewList.add(view);
	}

	public void refreshAllViews() {
		for (View v : getViewList()) v.refreshAllElementsView();
	}

	public void extendedDiscovery(int x, int y) {
		platJeu[x][y].setHide(false);
		if (platJeu[x][y].getNbMineProxim() == 0) {
			if (x - 1 >= 0) {
				if (y - 1 >= 0 && platJeu[x - 1][y - 1].isHide()) extendedDiscovery(x - 1, y - 1);
				if (platJeu[x - 1][y].isHide()) extendedDiscovery(x - 1, y);
				if (y + 1 < dimension.getY() && platJeu[x - 1][y + 1].isHide())
					extendedDiscovery(x - 1, y + 1);
			}
			if (y - 1 >= 0 && platJeu[x][y - 1].isHide()) extendedDiscovery(x, y - 1);
			if (y + 1 < dimension.getY() && platJeu[x][y + 1].isHide()) extendedDiscovery(x, y + 1);
			if (x + 1 < dimension.getX()) {
				if (y - 1 >= 0 && platJeu[x + 1][y - 1].isHide()) extendedDiscovery(x + 1, y - 1);
				if (platJeu[x + 1][y].isHide()) extendedDiscovery(x + 1, y);
				if (y + 1 < dimension.getY() && platJeu[x + 1][y + 1].isHide())
					extendedDiscovery(x + 1, y + 1);
			}
		}
	}

}
