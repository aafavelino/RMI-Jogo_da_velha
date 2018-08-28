package jogador;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import jogo.IJogo;

public class ClienteJogo {

	public static void executarPartida(IJogo jogo, Scanner sc) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException{
		int meuId;
		meuId = jogo.entrarNaPartida();
		System.out.println("Aguardando Jogadores...");
		while (!jogo.isPronto()) {
			System.out.println("...");
			Thread.sleep(500);
		}
		System.out.println("Jogador encontrado!");

		while (!jogo.isPartidaEncerrada()) {
			if (jogo.isMinhaVez(meuId)) {
				System.out.println(jogo.getTabuleiro());
				System.out.println("Faça sua jogada, informe a linha");
				int linha = sc.nextInt();
				System.out.println("Agora informe a coluna");
				int coluna = sc.nextInt();

				if (jogo.isJogadaValida(meuId, linha, coluna)) {
					jogo.realizarJogada(meuId, linha, coluna);
					System.out.println(jogo.getTabuleiro());
					if(!jogo.isPartidaEncerrada())
						System.out.println("Aguardando Jogada do Oponente...");
				} else {
					System.out.println("Jogada Inválida, tente novamente!");
				}
			}
			Thread.sleep(100);
		}
		
		if (jogo.resultadoPartida() ==  meuId) {
			System.out.println("Parabéns jogador você ganhou um jogo da velha");
		} else if(jogo.resultadoPartida() == 0){
			System.out.println("Empate! sOu você não joga bem, ou ele não joga bem, ou os dois jogam mal!");
		} else {
			System.out.println("Perdeu! Precisa aprender a jogar...!");
		}
		
		System.out.println("Você deseja começar uma nova partida digite 1, senão digite 0");
		if(sc.nextInt() == 1) {
			jogo.newGame();
			executarPartida(jogo, sc);
		}
	}
	
	public static void main(String[] args)
			throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
		IJogo jogo = (IJogo) Naming.lookup("rmi://localhost/Jogo");

		Scanner sc = new Scanner(System.in);
		System.out.println("Bem vindo ao jogo da velha!");

		System.out.println(" Se você deseja se juntar a uma partida digite 1");
			
		int iniciarPartida = sc.nextInt();

		if (iniciarPartida == 1) {
			executarPartida(jogo, sc);
			
			sc.close();
		}
	}
}