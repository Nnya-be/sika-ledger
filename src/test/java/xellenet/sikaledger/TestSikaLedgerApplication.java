package xellenet.sikaledger;

import org.springframework.boot.SpringApplication;

public class TestSikaLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.from(SikaLedgerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
