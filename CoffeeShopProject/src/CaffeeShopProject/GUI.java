package CaffeeShopProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class GUI {

	protected Shell shell;
	private Text txtMenu;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GUI window = new GUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 600);
		shell.setText("SWT Application");
		
		Button btnNewCustomer = new Button(shell, SWT.NONE);
		btnNewCustomer.setBounds(198, 10, 109, 26);
		btnNewCustomer.setText("New Customer");
		
		txtMenu = new Text(shell, SWT.BORDER);
		txtMenu.setText("Menu");
		txtMenu.setBounds(10, 10, 51, 26);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(9, 75, 298, 196);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		List list = new List(scrolledComposite, SWT.BORDER);
		scrolledComposite.setContent(list);
		scrolledComposite.setMinSize(list.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Button btnRemove = new Button(shell, SWT.NONE);
		btnRemove.setBounds(10, 277, 66, 30);
		btnRemove.setText("Remove");
		
		Spinner spinner = new Spinner(shell, SWT.BORDER);
		spinner.setBounds(91, 277, 59, 26);
		
		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.setBounds(168, 277, 51, 30);
		btnAdd.setText("Add");
		
		Button btnPurchase = new Button(shell, SWT.NONE);
		btnPurchase.setBounds(237, 277, 70, 30);
		btnPurchase.setText("Purchase");
		
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setBounds(360, 10, 177, 196);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		
		List list_1 = new List(scrolledComposite_1, SWT.BORDER);
		scrolledComposite_1.setContent(list_1);
		scrolledComposite_1.setMinSize(list_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ScrolledComposite scrolledComposite_1_1 = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1_1.setExpandVertical(true);
		scrolledComposite_1_1.setExpandHorizontal(true);
		scrolledComposite_1_1.setBounds(360, 222, 177, 321);
		
		List list_2 = new List(scrolledComposite_1_1, SWT.BORDER);
		scrolledComposite_1_1.setContent(list_2);
		scrolledComposite_1_1.setMinSize(list_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ScrolledComposite scrolledComposite_1_2 = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1_2.setExpandVertical(true);
		scrolledComposite_1_2.setExpandHorizontal(true);
		scrolledComposite_1_2.setBounds(559, 10, 213, 533);
		
		List list_3 = new List(scrolledComposite_1_2, SWT.BORDER);
		scrolledComposite_1_2.setContent(list_3);
		scrolledComposite_1_2.setMinSize(list_3.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		text = new Text(shell, SWT.BORDER);
		text.setText("Item Description");
		text.setBounds(10, 326, 297, 60);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setText("Tax");
		text_1.setBounds(10, 405, 190, 30);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setText("Subtotal");
		text_2.setBounds(10, 441, 190, 30);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setText("Discount");
		text_3.setBounds(10, 513, 190, 30);
		
		text_4 = new Text(shell, SWT.BORDER);
		text_4.setText("Total");
		text_4.setBounds(10, 477, 190, 30);
		
		Button btnMeal = new Button(shell, SWT.NONE);
		btnMeal.setBounds(9, 42, 66, 26);
		btnMeal.setText("Meal");
		
		Button btnMeal_1 = new Button(shell, SWT.NONE);
		btnMeal_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnMeal_1.setText("Desserts");
		btnMeal_1.setBounds(168, 43, 66, 26);
		
		Button btnMeal_1_1 = new Button(shell, SWT.NONE);
		btnMeal_1_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnMeal_1_1.setText("Drinks");
		btnMeal_1_1.setBounds(241, 43, 66, 26);
		
		Button btnMeal_1_2 = new Button(shell, SWT.NONE);
		btnMeal_1_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnMeal_1_2.setText("Sides");
		btnMeal_1_2.setBounds(84, 43, 66, 26);
		
		Button btnOrderList = new Button(shell, SWT.NONE);
		btnOrderList.setBounds(217, 403, 90, 30);
		btnOrderList.setText("Order List");
		
		Button btnReceipt = new Button(shell, SWT.NONE);
		btnReceipt.setBounds(217, 456, 90, 30);
		btnReceipt.setText("Receipt");
		
		Button btnFinalReportCoffee = new Button(shell, SWT.NONE);
		btnFinalReportCoffee.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnFinalReportCoffee.setBounds(217, 511, 90, 30);
		btnFinalReportCoffee.setText("Final Report");

	}
}

