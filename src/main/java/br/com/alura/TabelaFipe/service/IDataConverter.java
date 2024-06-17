package br.com.alura.TabelaFipe.service;

public interface IDataConverter {
    <T> T getData(String json, Class<T> className);
}
