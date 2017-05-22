package demineur.view;

import demineur.model.Data;

import javax.swing.*;

import static demineur.model.Data.NB_FLAG_MAX_SUPPL_AUTORISE;

/**
 * Created by Valentin.
 */
public class ViewStatMine extends JPanel implements View {

	private Data data;

	private JLabel nbMine, nbGoodFlag, nbFlag;

	public ViewStatMine(Data data) {
		this.data = data;
		init();
	}

	private void init() {
		nbMine = new JLabel("");
		nbMine.setIcon(data.getJeuImg("mine"));
		nbMine.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));

		nbGoodFlag = new JLabel("");
		nbGoodFlag.setIcon(data.getJeuImg("goodflag"));
		nbGoodFlag.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));

		nbFlag = new JLabel("");
		nbFlag.setIcon(data.getJeuImg("flag"));
		add(nbMine);
		add(nbGoodFlag);
		add(nbFlag);
		refreshAllElementsView();
	}

	@Override
	public void refreshAllElementsView() {
		nbMine.setText(String.valueOf(data.getNbMine()));
		int[] flag = data.getNbFlagAndGoodFlag();
		nbGoodFlag.setText(String.valueOf(flag[1]));
		nbFlag.setText(flag[0] + "/" + (data.getNbMine() + NB_FLAG_MAX_SUPPL_AUTORISE));
	}
}
