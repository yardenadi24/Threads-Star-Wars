package bgu.spl.mics.application;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		//--create Diary--//
		Diary diary = Diary.getInstance();
		String path = args[0];
		//store json data in object called JsonInput
		JsonInPut newInput = new Gson().fromJson(new FileReader(path), JsonInPut.class);
		//store relevant data from newInput

		Attack[] attacks = newInput.getAttacks();
		int R2D2 = newInput.getR2D2();
		int Lando = newInput.getLando();
		Ewoks ewoksArr = Ewoks.getInstance();
		int Ewoks = newInput.getEwoks();
		for(int i=0; i<Ewoks;i++){
			ewoksArr.addEwok(new Ewok(i+1));
		}




		//---create Microservices(Tasks)---//
		HanSoloMicroservice HanSoloMicro = new HanSoloMicroservice();
		C3POMicroservice C3POMicro = new C3POMicroservice();
		LandoMicroservice LandoMicro = new LandoMicroservice(Lando);
		R2D2Microservice R2D2Micro = new R2D2Microservice(R2D2);
		LeiaMicroservice LeiaMicro = new LeiaMicroservice(attacks);

		//---create threads---//
		Thread HanSoloThread = new Thread(HanSoloMicro);
		Thread C3POThread = new Thread(C3POMicro);
		Thread LandoThread = new Thread(LandoMicro);
		Thread R2D2Thread = new Thread(R2D2Micro);
		Thread LeiaThread = new Thread(LeiaMicro);




		//---run threads---//
		HanSoloThread.start();
		C3POThread.start();
		LeiaThread.start();
		LandoThread.start();
		R2D2Thread.start();



		HanSoloThread.join();
		C3POThread.join();
		LeiaThread.join();
		R2D2Thread.join();
		LandoThread.join();


		JsonOutPut jsonOut = new JsonOutPut();
		jsonOut.setTotalAttacks(diary.GetTotalAttacks().get());
		jsonOut.setHanSoloFinish(diary.GetHanSoloFinish());
		jsonOut.setC3POFinish(diary.GetC3POFinish());
		jsonOut.setR2D2Deactivate(diary.GetR2D2Deactivate());
		jsonOut.setLeiaTerminate(diary.GetLeiaTerminate());
		jsonOut.setHanSoloTerminate(diary.GetHanSoloTerminate());
		jsonOut.setC3POTerminate(diary.GetC3POTerminate());
		jsonOut.setR2D2Terminate(diary.GetR2D2Terminate());
		jsonOut.setLandoTerminate(diary.GetLandoTerminate());


		FileWriter fw=new FileWriter(args[1]);
		Gson gson = new Gson();
		gson.toJson(jsonOut,fw);
		fw.close();
	}
}

