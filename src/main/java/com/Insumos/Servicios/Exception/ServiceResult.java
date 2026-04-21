package com.Insumos.Servicios.Exception;

import java.util.List;


public class ServiceResult<T> {
    
    public boolean correct;
    public int status;
    public String ErrorMesage;
    public T object;
    public List<T> objects;
    public Exception ex;
    public String message;
    
    
}
