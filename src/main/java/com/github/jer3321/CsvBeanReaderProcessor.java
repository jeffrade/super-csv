package com.github.jer3321;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class CsvBeanReaderProcessor<T> {

	private static final Logger LOG = LoggerFactory.getLogger(CsvBeanReaderProcessor.class);
	
	private CellProcessor[] processors;	
	private Class<T> clazz;	
	private InputStream input;
	private String[] header;
	
	public CsvBeanReaderProcessor(CellProcessor[] processors, Class<T> clazz, InputStream input, String[] header){
		super();
		this.processors = processors.clone();
		this.clazz = clazz;
		this.input = input;
		this.header = header.clone();
	}
	
	public List<T> processCsvBeanReader() throws Exception{
		ICsvBeanReader beanReader = null;
		List<T> beans = new ArrayList<T>();
		InputStreamReader inputStreamReader = new InputStreamReader(input);
        try {
            beanReader = new CsvBeanReader(inputStreamReader, CsvPreference.STANDARD_PREFERENCE);
            beanReader.getHeader(true); // skip the header
            
            T bean;
            do{
            	try{
            		bean = beanReader.read(clazz, header, processors);
            	} catch (Exception e){
            		throw new Exception("Error when reading line [" + beanReader.getUntokenizedRow() + "] from row number " + beanReader.getLineNumber(), e);
            	}
            	if(bean != null){
            		beans.add(bean);
            	}
            } while( bean != null );
        } catch (IOException e){
        	LOG.error("Error reading input stream", e);
        } finally {
            if( beanReader != null ) {
            	try {
            		beanReader.close();
				} catch (IOException e) {
		        	LOG.error("Error when closing beanReader", e);
				}
            }
        }
        
        return beans;
	}
}
