package demineur.view;

import demineur.model.Data;
import demineur.model.PlateauSize;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by Valentin.
 */
public class ViewPopupSelectDimension extends JPanel implements View {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private Data data;

	private JSlider slidNbLigne, slidNbColonne;

	public ViewPopupSelectDimension(Data data) {
		this.data = data;
		init();
	}

	private void init() {
		JPanel dimX = new JPanel();
		dimX.setBorder(BorderFactory.createTitledBorder("Nombre de ligne"));

		final JLabel nbLigne = new JLabel();

		slidNbLigne = new JSlider(10, 30);
		slidNbLigne.setPaintTicks(true);
		slidNbLigne.setPaintLabels(true);
		slidNbLigne.setMajorTickSpacing(10);
		slidNbLigne.setMinorTickSpacing(5);
		slidNbLigne.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				nbLigne.setText(slidNbLigne.getValue() + " lignes");
			}
		});
		slidNbLigne.setValue(10);
		dimX.add(slidNbLigne);
		dimX.add(nbLigne);


		JPanel dimY = new JPanel();
		dimY.setBorder(BorderFactory.createTitledBorder("Nombre de colonne"));

		final JLabel nbColonne = new JLabel();

		slidNbColonne = new JSlider(10, 60);
		slidNbColonne.setPaintTicks(true);
		slidNbColonne.setPaintLabels(true);
		slidNbColonne.setMajorTickSpacing(10);
		slidNbColonne.setMinorTickSpacing(5);
		slidNbColonne.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				nbColonne.setText(slidNbColonne.getValue() + " colonnes");
			}
		});
		slidNbColonne.setValue(10);
		dimY.add(slidNbColonne);
		dimY.add(nbColonne);

		add(dimX);
		add(dimY);
	}

	public PlateauSize getPlateauDimension() {
		return new PlateauSize(slidNbLigne.getValue(), slidNbColonne.getValue());
	}

	@Override
	public void refreshAllElementsView() {

	}
}
