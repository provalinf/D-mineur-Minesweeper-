package demineur.controller;

import demineur.model.Data;
import demineur.view.ViewPlateau;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static demineur.model.Data.NB_FLAG_MAX_SUPPL_AUTORISE;

/**
 * Created by Valentin.
 */
public class BMine extends MouseAdapter {

	private Data data;
	private int x, y;

	public BMine(Data data, int x, int y) {
		this.data = data;
		this.x = x;
		this.y = y;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!data.getMineXY(x, y).isHide()) return;
		if (data.verifJeuGagne()) return;
		if (data.isJeuPerdu()) return;

		if (SwingUtilities.isRightMouseButton(e)) {
			if (data.getNbFlagAndGoodFlag()[0] - NB_FLAG_MAX_SUPPL_AUTORISE < data.getNbMine()) {
				data.getMineXY(x, y).setFlag(!data.getMineXY(x, y).isFlag());
			} else if (data.getMineXY(x, y).isFlag()) {    // Cas de déflag
				data.getMineXY(x, y).setFlag(!data.getMineXY(x, y).isFlag());
			}

		} else if (SwingUtilities.isLeftMouseButton(e)) {
			if (data.getMineXY(x, y).isMine()) {
				data.setJeuPerdu();
			} else if (data.getMineXY(x, y).getNbMineProxim() == 0) {
				data.playSound("discov");
				data.extendedDiscovery(x, y);
			}
			data.getMineXY(x, y).setHide(false);

		}

		if (data.isJeuPerdu()) data.unHideAllBomb();    // Requiert le refreshAllView

		data.refreshAllViews();

		if (data.verifJeuGagne() || data.isJeuPerdu()) {
			int res = 1;
			if (data.verifJeuGagne()) {
				data.playSound("finish");
				Object[] options = {"Quitter", "Rejouer"};
				res = JOptionPane.showOptionDialog(null, "Félicitation pauvre fou, vous êtes parvenu à déminer sans mourir.", "Partie gagné", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			}

			if (data.isJeuPerdu()) {
				Object[] options = {"Quitter", "Rejouer", "Recommencer cette partie"};
				res = JOptionPane.showOptionDialog(null, "Vous vous êtes fait sauter et êtes mort dans d'atroces souffrances. Voulez-vous ressusciter ?", "Partie perdue", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			}

			if (res == 0) {
				System.exit(0);
			} else if (res == 2) {
				ViewPlateau plateau = (ViewPlateau) data.getViewId(2);
				data.resetSamePart();
				plateau.defineDimensionPlat();
				data.refreshAllViews();
			} else {
				ViewPlateau plateau = (ViewPlateau) data.getViewId(2);
				data.initAndReset();
				plateau.defineDimensionPlat();
				data.refreshAllViews();
			}
		}


	}

}
