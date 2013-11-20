package com.github.jer3321;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.github.jer3321.model.Student;

public class App {
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
    public static void main(String[] args) {
		try {
	    	InputStream input = getMockInputStream();//If working with a MultipartFile, you can just assign MultipartFile.getInputStream()
	    	CsvBeanReaderProcessor<Student> processor = new CsvBeanReaderProcessor<Student>(
	    			new CellProcessor[] {new NotNull()
	    				, new NotNull()
	    				, new Optional()
	    				, new Optional()
	    				, new Optional(new ParseDate("MM/dd/yyyy"))
	    				, new Optional(new ParseDouble())}
	    			, Student.class
	    			, input
	    			, new String[] {"firstName","lastName","email","phone","birthday","grade"});
	    	List<Student> students = processor.processCsvBeanReader();
	    	
	    	for(Student student : students){
	    		LOG.info(student.toString());
	    	}
		} catch (Exception e) {
			LOG.error("", e);
		}
    }

    private static InputStream getMockInputStream() throws IOException {
		File file = File.createTempFile("foo", "csv");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("firstName,lastName,email,phone,birthday,grade\n" +
				"John,Doe,jdoe@example.com,555-1234,01/01/1995,10.0\n" +
				"Jane,Smith,jsmith@example.com,555-4321,12/31/1994,11.0");
		fileWriter.close();
		
		return new FileInputStream(file);
	}
}
