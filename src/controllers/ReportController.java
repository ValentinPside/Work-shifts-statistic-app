/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import models.Shift;
import models.resurces.Reader;
import stages.ExceptionStage;
import stages.SuccessStage;

/**
 * FXML Controller class
 *
 * @author Valentin
 */
public class ReportController implements Initializable {

    @FXML
    private ComboBox<String> shiftName;
    
    @FXML
    private DatePicker date1;
    
    @FXML
    private DatePicker date2;
    
    @FXML
    private TextArea textArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list1 = FXCollections.observableArrayList("Все смены", "А", "Б", "В", "Г");
        shiftName.setItems(list1);
    }    
    
    @FXML
    private void createReport(javafx.event.ActionEvent event) throws IOException{
        if(checkShiftDate(shiftName, date1, date2)){
            String currentShiftName = shiftName.getValue();
            LocalDate localDate1 = date1.getValue();
            LocalDate localDate2 = date2.getValue();
            if(currentShiftName.equals("Все смены")){
                Shift a = new Shift("А");
                new Reader(a);
                Shift b = new Shift("Б");
                new Reader(b);
                Shift c = new Shift("В");
                new Reader(c);
                Shift d = new Shift("Г");
                new Reader(d);
                Shift[] shifts = new Shift[]{a, b ,c ,d};
                reportWriter(shifts, localDate1, localDate2);
            }
            else{
                Shift shift = new Shift(currentShiftName);
                new Reader(shift);
                shiftReportWriter(shift, localDate1, localDate2);
            }
        }
    }
    
    private boolean checkShiftDate(ComboBox<String> shiftName, DatePicker date1, DatePicker date2) throws IOException{
        
        if(shiftName.getValue() == null){
            new ExceptionStage("Смена не выбрана!");
            return false;
        }
        
        if(date1.getValue() == null || date2.getValue() == null){
            new ExceptionStage("Даты не выбраны!");
            return false;
        }
        
        if(date1.getValue().isAfter(date2.getValue())){
            new ExceptionStage("Даты выбраны неверно!");
            return false;
        }
        
        return true;
    }
    
    private boolean reportWriter(Shift[] shifts, LocalDate localDate1, LocalDate localDate2) throws IOException {
        String reportDay1 = String.valueOf(localDate1);
        String reportDay2 = String.valueOf(localDate2);
        LocalDate originalLocalDate1 = localDate1;
        LocalDate originalLocalDate2 = localDate2;
        int waterValue = 0;
        int hoursValue = 0;
        int expenditureValue = 0;
        for(Shift i : shifts){
            
            ArrayList<LocalDate> date = i.getDate();
            localDate1 = originalLocalDate1;
            localDate2 = originalLocalDate2;
            
            if(!date.contains(localDate1)){
                while(!date.contains(localDate1) && localDate1.isBefore(date.get(date.size() - 1))){
                    localDate1 = localDate1.plusDays(1);
                }
            }
            
            if(!date.contains(localDate2)){
                while(!date.contains(localDate2) && localDate2.isAfter(localDate1)){
                    localDate2 = localDate2.minusDays(1);
                }
            }
            
            if(!date.contains(localDate1) && !date.contains(localDate2)){
                continue;
            }
            
            if(localDate1.isEqual(localDate2)){
                int index = date.indexOf(localDate1);
                waterValue = waterValue + i.getWater().get(index);
                hoursValue = hoursValue + i.getHours().get(index);
                expenditureValue = expenditureValue + i.getExpenditure().get(index); 
                continue;
            }
            
            int index1 = date.indexOf(localDate1);
            int index2 = date.indexOf(localDate2);

            ArrayList<Integer> water = i.getWater();
            ArrayList<Integer> hours = i.getHours();
            ArrayList<Integer> expenditure = i.getExpenditure();
            for(int a = index1; a <= index2; a++){
                waterValue = waterValue + water.get(a);
                hoursValue = hoursValue + hours.get(a);
                expenditureValue = expenditureValue + expenditure.get(a);
            }
        }
        textArea.setText("Отчёт работы ВВЧ с " + reportDay1 + " по " + reportDay2 + "\n" +
                "Наработка по кубометрам составила " + waterValue + " метров кубических;\n" + 
                "Наработка по часам составила " + hoursValue + " часов;\n" +
                "Выдача составила порядка " + expenditureValue + " метров кубических;\n");
        new SuccessStage("Отчёт успешно составлен!");
        return true;
    }
    
    private boolean shiftReportWriter(Shift shift, LocalDate localDate1, LocalDate localDate2) throws IOException {
        String reportDay1 = String.valueOf(localDate1);
        String reportDay2 = String.valueOf(localDate2);
        int waterValue = 0;
        int hoursValue = 0;
        int expenditureValue = 0;
        ArrayList<LocalDate> date = shift.getDate();
        if(!date.contains(localDate1)){
            while(!date.contains(localDate1) && localDate1.isBefore(date.get(date.size() - 1))){
                localDate1 = localDate1.plusDays(1);
            }
        }
        if(!date.contains(localDate2)){
            while(!date.contains(localDate2) && localDate2.isAfter(localDate1)){
                localDate2 = localDate2.minusDays(1);
            }
        }
        if(!date.contains(localDate1) && !date.contains(localDate2)){
           new ExceptionStage("В выбранном диапазоне дат смена не работала!");
            return false; 
        }
        if(localDate1.isEqual(localDate2)){
            int index = date.indexOf(localDate1);
            waterValue = shift.getWater().get(index);
            hoursValue = shift.getHours().get(index);
            expenditureValue = shift.getExpenditure().get(index);
            textArea.setText("Отчёт работы смены " + shift.getShiftName() + " с " + reportDay1 + " по " + reportDay2 + "\n" +
                "Наработка по кубометрам составила " + waterValue + " метров кубических;\n" + 
                "Наработка по часам составила " + hoursValue + " часов;\n" +
                "Выдача составила порядка " + expenditureValue + " метров кубических;\n");
            new SuccessStage("Отчёт успешно составлен!");
            return true;
        }
        int index1 = date.indexOf(localDate1);
        int index2 = date.indexOf(localDate2);
        ArrayList<Integer> water = shift.getWater();
        ArrayList<Integer> hours = shift.getHours();
        ArrayList<Integer> expenditure = shift.getExpenditure();
        for(int a = index1; a <= index2; a++){
            waterValue = waterValue + water.get(a);
            hoursValue = hoursValue + hours.get(a);
            expenditureValue = expenditureValue + expenditure.get(a);
        }
        textArea.setText("Отчёт работы смены " + shift.getShiftName() + " с " + reportDay1 + " по " + reportDay2 + "\n" +
                "Наработка по кубометрам составила " + waterValue + " метров кубических;\n" + 
                "Наработка по часам составила " + hoursValue + " часов;\n" +
                "Выдача составила порядка " + expenditureValue + " метров кубических;\n");
        new SuccessStage("Отчёт успешно составлен!");
        return true;
    }
}
