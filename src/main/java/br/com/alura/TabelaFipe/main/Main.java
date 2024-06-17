package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.model.Data;
import br.com.alura.TabelaFipe.model.Model;
import br.com.alura.TabelaFipe.service.ApiConsumer;
import br.com.alura.TabelaFipe.service.DataConverter;

import java.util.Comparator;
import java.util.Scanner;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private ApiConsumer apiConsumer = new ApiConsumer();
    private DataConverter converter = new DataConverter();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void showMenu(){
        var menu = """
                Selecione uma opção:
                1 - Carro
                2 - Moto
                3 - Caminhão
                
                Digite o número da opção que corresponde ao tipo de veículo que você deseja consultar:""";

        System.out.println(menu);

        var option = 0;

        while (option < 1 || option > 3){
            option = scanner.nextInt();
            if(option < 1 || option > 3){
                System.out.println("Opção inválida. Tente novamente");
                System.out.println(menu);
            }
        }

        String url = null;

        switch (option){
            case 1:
                url = URL_BASE + "carros/marcas/";
                break;
            case 2:
                url = URL_BASE + "motos/marcas/";
                break;
            case 3:
                url = URL_BASE + "caminhoes/marcas/";
        }

        var json = apiConsumer.getData(url);
        var brands = converter.getList(json, Data.class);
        brands.stream()
                .sorted(Comparator.comparing(Data::code))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca que deseja consultar:");
        var code = scanner.nextInt();

        url += code + "/modelos";

        json = apiConsumer.getData(url);
        var models = converter.getData(json, Model.class);

        System.out.println("Modelos da marca escolhida:");
        models.modelsList().stream()
                .sorted(Comparator.comparing(Data::name))
                .forEach(System.out::println);

    }

}
