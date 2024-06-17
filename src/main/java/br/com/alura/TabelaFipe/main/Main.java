package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.model.Data;
import br.com.alura.TabelaFipe.model.Model;
import br.com.alura.TabelaFipe.model.Vehicle;
import br.com.alura.TabelaFipe.service.ApiConsumer;
import br.com.alura.TabelaFipe.service.DataConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        System.out.println("\nInforme o código da marca que deseja consultar:");
        var code = scanner.nextInt();
        scanner.nextLine();

        url += code + "/modelos/";

        json = apiConsumer.getData(url);
        var models = converter.getData(json, Model.class);

        System.out.println("\nModelos da marca escolhida:");
        models.modelsList().stream()
                .sorted(Comparator.comparing(Data::name))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do carro a ser consultado:");
        var vehicleName = scanner.nextLine();

        List<Data> filteredModels = models.modelsList().stream()
                .filter(m -> m.name().toLowerCase().contains(vehicleName))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados:");
        filteredModels.forEach(System.out::println);

        System.out.println("Digite o código do modelo para buscar os valores de avaliação: ");
        var modelCode = scanner.nextLine();

        url += modelCode + "/anos/";

        json = apiConsumer.getData(url);
        List<Data> years = converter.getList(json,  Data.class);
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < years.size(); i++) {
            var urlYears = url + years.get(i).code();
            json = apiConsumer.getData(urlYears);
            Vehicle vehicle = converter.getData(json, Vehicle.class);
            vehicles.add(vehicle);
        }

        System.out.println("Veículos filtrados com avaliações por ano:");
        vehicles.forEach(System.out::println);




    }

}
