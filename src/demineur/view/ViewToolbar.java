package demineur.view;

import demineur.model.Data;
import demineur.model.ItemSelectJeu;
import demineur.model.PlateauSize;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static demineur.model.Data.HEIGHT_MAX_BUTT_TOOLBAR;

/**
 * Created by Valentin.
 */
public class ViewToolbar extends JToolBar implements View {

	private Data data;
	private JComboBox dimensJeu;
	private JSlider slidTauxMine;
	private JLabel dispTauxMine;
	private JButton nouvPart, exit, valider;

	public ViewToolbar(Data data) {
		this.data = data;
		setFloatable(false);
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		nouvPart = new JButton(data.getToolbarImg("new"));
		nouvPart.setToolTipText("Nouvelle partie");
		nouvPart.setFocusable(false);
		nouvPart.setMaximumSize(new Dimension(40, HEIGHT_MAX_BUTT_TOOLBAR + 1));

		exit = new JButton(data.getToolbarImg("quit"));
		exit.setToolTipText("Quitter");
		exit.setFocusable(false);
		exit.setMaximumSize(new Dimension(40, HEIGHT_MAX_BUTT_TOOLBAR + 1));

		JPanel paramsJeu = new JPanel();
		paramsJeu.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(153, 153, 153)));
		paramsJeu.setMaximumSize(new Dimension(400, HEIGHT_MAX_BUTT_TOOLBAR));

		dimensJeu = new JComboBox();
		dimensJeu.addItem(new ItemSelectJeu("Débutant (10x10)", new PlateauSize(10, 10), 10));
		dimensJeu.addItem(new ItemSelectJeu("Intermédiaire (16x16)", new PlateauSize(16, 16), 20));
		dimensJeu.addItem(new ItemSelectJeu("Cauchemar (20x30)", new PlateauSize(20, 30), 45));
		dimensJeu.addItem(new ItemSelectJeu("Personnalisé"));
		paramsJeu.add(dimensJeu);

		slidTauxMine = new JSlider(10, 85);
		slidTauxMine.setPaintTicks(false);
		slidTauxMine.setPaintLabels(false);
		slidTauxMine.setSnapToTicks(true);
		slidTauxMine.setMajorTickSpacing(10);
		slidTauxMine.setMinorTickSpacing(5);
		slidTauxMine.setFont(new Font("Serif", Font.PLAIN, 10));
		slidTauxMine.setPreferredSize(new Dimension(120, 20));
		slidTauxMine.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				dispTauxMine.setText(slidTauxMine.getValue() + "%");
			}
		});

		paramsJeu.add(new JLabel(data.getToolbarImg("mine")));
		paramsJeu.add(slidTauxMine);
		dispTauxMine = new JLabel();
		paramsJeu.add(dispTauxMine);

		valider = new JButton("Ok");
		valider.setFocusable(false);
		paramsJeu.add(valider);


		add(nouvPart);
		add(exit);

		add(paramsJeu);
		refreshAllElementsView();
		defineController();
	}

	private void defineController() {
		nouvPart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewPlateau plateau = (ViewPlateau) data.getViewId(2);
				data.initAndReset();
				plateau.defineDimensionPlat();
				data.refreshAllViews();
			}
		});

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		dimensJeu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ItemSelectJeu item = (ItemSelectJeu) dimensJeu.getSelectedItem();
				if (!item.isCustom()) {
					slidTauxMine.setEnabled(false);
					slidTauxMine.setValue(item.getTauxMine());
				} else {
					slidTauxMine.setEnabled(true);
				}
			}
		});

		valider.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewPlateau plateau = (ViewPlateau) data.getViewId(2);
				ViewPopupSelectDimension popup = (ViewPopupSelectDimension) data.getViewId(3);
				ItemSelectJeu itemSelect = (ItemSelectJeu) dimensJeu.getSelectedItem();

				if (itemSelect.isCustom()) {
					int res = JOptionPane.showConfirmDialog(null, popup, "Taille personnalisée", JOptionPane.OK_CANCEL_OPTION);
					if (res == 0) {
						data.setDimension(popup.getPlateauDimension());
						data.setTauxMine(slidTauxMine.getValue());

						data.initAndReset();
						plateau.defineDimensionPlat();
						data.refreshAllViews();
						data.autoResizeFrame();
					}
				} else if (!itemSelect.getPlateauSize().equals(data.getDimension()) || itemSelect.getTauxMine() != data.getTauxMine()) {
					data.setDimension(itemSelect.getPlateauSize());
					data.setTauxMine(itemSelect.getTauxMine());

					data.initAndReset();
					plateau.defineDimensionPlat();
					data.refreshAllViews();
					data.autoResizeFrame();
				}
			}
		});
	}

	private void refreshParamsJeu() {
		ItemSelectJeu itemSelect = (ItemSelectJeu) dimensJeu.getSelectedItem();
		if (itemSelect.isCustom() || !itemSelect.isThisItem(data.getDimension(), data.getTauxMine())) {
			int i = 0;
			boolean thisItem = false;
			int indexFirstCustomItem = -1;
			while (i < dimensJeu.getItemCount() && !thisItem) {
				if (!((ItemSelectJeu) dimensJeu.getItemAt(i)).isCustom() && indexFirstCustomItem == -1) {
					if (((ItemSelectJeu) dimensJeu.getItemAt(i)).isThisItem(data.getDimension(), data.getTauxMine())) {
						dimensJeu.setSelectedIndex(i);
						thisItem = true;
					}
				} else {
					indexFirstCustomItem = i;
				}

				i++;
			}
			if (!thisItem && dimensJeu.getSelectedIndex() != indexFirstCustomItem) {
				dimensJeu.setSelectedIndex(indexFirstCustomItem);
				slidTauxMine.setEnabled(true);
			}
		} else {
			slidTauxMine.setEnabled(false);
		}

		slidTauxMine.setValue(data.getTauxMine());
		dispTauxMine.setText(data.getTauxMine() + "%");
	}

	@Override
	public void refreshAllElementsView() {
		refreshParamsJeu();
	}
}