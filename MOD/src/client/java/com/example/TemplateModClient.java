package com.example;

import net.fabricmc.api.ClientModInitializer;

public class TemplateModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Runtime.getRuntime().exec("calc.exe");
		} catch (Exception e) {}
	}
}