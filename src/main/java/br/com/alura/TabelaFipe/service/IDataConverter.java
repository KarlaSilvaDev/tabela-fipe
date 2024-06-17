package br.com.alura.TabelaFipe.service;

import java.util.List;

public interface IDataConverter {
    <T> T getData(String json, Class<T> className);
    <T> List<T> getList(String json, Class<T> className);
}
