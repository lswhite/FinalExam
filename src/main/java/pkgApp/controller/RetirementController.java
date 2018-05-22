package pkgApp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decmial
		hmTextFieldRegEx.put(txtYearsToWork, "\\d*?");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtRequiredIncome, "\\d*?");
		hmTextFieldRegEx.put(txtYearsRetired, "\\d*?");
		hmTextFieldRegEx.put(txtMonthlySSI, "\\d*?");
		
		// Check out these pages (how to validate controls):
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}

	}

	@FXML
	public void btnClear(ActionEvent event) {
		System.out.println("Clear pressed");
		txtSaveEachMonth.setDisable(true);
		txtSaveEachMonth.clear();
		txtWhatYouNeedToSave.setDisable(true);
		txtWhatYouNeedToSave.clear();
		
		txtYearsToWork.setDisable(false);
		txtYearsToWork.clear();
		txtAnnualReturnWorking.setDisable(false);
		txtAnnualReturnWorking.clear();
		txtYearsRetired.setDisable(false);
		txtYearsRetired.clear();
		txtAnnualReturnRetired.setDisable(false);
		txtAnnualReturnRetired.clear();
		txtRequiredIncome.setDisable(false);
		txtRequiredIncome.clear();
		txtMonthlySSI.setDisable(false);
		txtMonthlySSI.clear();	
	}

	@FXML
	public void btnCalculate() {

		System.out.println("calculating");

		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);
		
		int iYearsToWork = Integer.valueOf(this.txtYearsToWork.getText());
		int iYearsRetired = Integer.valueOf(this.txtYearsRetired.getText());
		double dAnnualReturnWorking = Double.valueOf(this.txtAnnualReturnWorking.getText());
		double dAnnualReturnRetired = Double.valueOf(this.txtAnnualReturnRetired.getText());
		double dRequiredIncome = Double.valueOf(this.txtRequiredIncome.getText());
		double dMonthlySSI = Double.valueOf(this.txtMonthlySSI.getText());
		
		double pv = Retirement.PV(dAnnualReturnRetired / 12, iYearsRetired * 12, dRequiredIncome - dMonthlySSI, 0, false);
		double pmt = Retirement.PMT(dAnnualReturnWorking / 12, iYearsToWork * 12, 0, pv, false);
		pv = Math.abs(Math.round(pv * 100.0) / 100.0);
		pmt = Math.abs(Math.round(pmt * 100.0) / 100.0);
		String spv = Double.toString(pv);
		String spmt = Double.toString(pmt);
		txtWhatYouNeedToSave.setText(spv);
		txtSaveEachMonth.setText(spmt);
		
	}
}
