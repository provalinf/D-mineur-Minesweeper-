package demineur;

import demineur.model.Data;
import demineur.view.ViewPlateau;
import demineur.view.ViewPopupSelectDimension;
import demineur.view.ViewStatMine;
import demineur.view.ViewToolbar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Valentin.
 */
public class Main extends JFrame{

	private Main() {
		super("Démineur");
		setLayout(new BorderLayout());
		setResizable(false);

		Data data = new Data(this);
		setIconImage(data.getIconApp());

		data.addToViewList(new ViewToolbar(data));
		data.addToViewList(new ViewStatMine(data));
		data.addToViewList(new ViewPlateau(data));
		data.addToViewList(new ViewPopupSelectDimension(data));

		add((JToolBar)data.getViewId(0), BorderLayout.PAGE_START);
		add((ViewStatMine) data.getViewId(1), BorderLayout.CENTER);
		add((ViewPlateau) data.getViewId(2), BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Main();
	}
}
