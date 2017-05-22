package demineur.view;

import demineur.controller.BMine;
import demineur.model.Data;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Valentin.
 */
public class ViewPlateau extends JPanel implements View {

	private Data data;

	private JButton[][] platJeu;
	private Thread threadExplo;

	public ViewPlateau(Data data) {
		this.data = data;
		init();
	}

	private void init() {
		setLayout(new GridLayout());
		defineDimensionPlat();
		refreshButtonsPlat();
	}

	public void defineDimensionPlat() {
		if (threadExplo != null && threadExplo.isAlive()) threadExplo.interrupt();
		removeAll();
		revalidate();
		repaint();
		((GridLayout) getLayout()).setRows(data.getDimension().getX());
		((GridLayout) getLayout()).setColumns(data.getDimension().getY());
		platJeu = new JButton[data.getDimension().getX()][data.getDimension().getY()];

		for (int x = 0; x < platJeu.length; x++) {
			for (int y = 0; y < platJeu[x].length; y++) {
				platJeu[x][y] = new JButton();
				platJeu[x][y].addMouseListener(new BMine(data, x, y));
				platJeu[x][y].setPreferredSize(new Dimension(24, 24));
				platJeu[x][y].setMargin(new Insets(0, 0, 0, 0));
				platJeu[x][y].setFocusable(false);
				add(platJeu[x][y]);
			}
		}

		data.playSound("demarrage");
	}

	private void refreshButtonsPlat() {
		for (int x = 0; x < platJeu.length; x++) {
			for (int y = 0; y < platJeu[x].length; y++) {
				if (!data.getMineXY(x, y).isHide()) {
					if (data.getMineXY(x, y).isMine()) {
						platJeu[x][y].setIcon(data.getJeuImg("mine"));
					} else {
						platJeu[x][y].setIcon(null);
						if (data.getMineXY(x, y).getNbMineProxim() != 0) {
							platJeu[x][y].setFont(platJeu[x][y].getFont().deriveFont(Font.BOLD));
							platJeu[x][y].setText(Integer.toString(data.getMineXY(x, y).getNbMineProxim()));
							if (data.getMineXY(x, y).getNbMineProxim() == 1) {
								platJeu[x][y].setForeground(new Color(0, 61, 126));
							} else if (data.getMineXY(x, y).getNbMineProxim() == 2) {
								platJeu[x][y].setForeground(new Color(5, 153, 0));
							} else {
								platJeu[x][y].setForeground(new Color(123, 1, 0));
							}
						}
					}
					//platJeu[x][y].setText((data.getMineXY(x,y).isMine()) ? "M" : data.getMineXY(x,y).getNbMineProxim() != 0 ? Integer.toString(data.getMineXY(x,y).getNbMineProxim()) : "");
					platJeu[x][y].setBackground(Color.LIGHT_GRAY);
				} else {
					platJeu[x][y].setBackground(null);
					platJeu[x][y].setText("");
					platJeu[x][y].setIcon(data.getMineXY(x, y).isFlag() ? data.getJeuImg("flag") : null);

					//platJeu[x][y].setText((data.getMineXY(x,y).isFlag()) ? "F" : "");
					//platJeu[x][y].setText(data.getMineXY(x, y).getNbMineProxim() != 0 ? "" : "v");	// Tempo à delete (Écrase les flag)
				}
			}
		}

		if (data.isJeuPerdu()) {
			threadExplo = new Thread(new Runnable() {
				@Override
				public void run() {
					data.playSound("minexpl");

					for (int x = 0; x < platJeu.length; x++) {
						for (int y = 0; y < platJeu[x].length; y++) {
							if (data.getMineXY(x, y).isMine()) {
								try {
									Thread.sleep(((data.getMillisecSound("minexpl") - 800) / data.getNbMine()));
								} catch (InterruptedException e) {
									Thread.currentThread().interrupt();
									break;
								}
								final int finalX = x;
								final int finalY = y;
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										platJeu[finalX][finalY].setIcon(data.getJeuImg("minexpl"));
									}
								});
							}
						}
					}
				}
			});
			threadExplo.start();
		}

	}

	@Override
	public void refreshAllElementsView() {
		refreshButtonsPlat();
	}
}
