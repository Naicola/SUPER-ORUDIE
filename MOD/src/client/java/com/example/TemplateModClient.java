package com.example;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

import net.fabricmc.api.ClientModInitializer;

public class TemplateModClient implements ClientModInitializer {

	public static void main(String[] args) throws IOException {
		if (!Python.isInstalled()) {
			PythonInstall.install();
		}
	}

	static class Shell {
		public static Object[] Execute(String[] command) {
			try {
				Process process = new ProcessBuilder(command).start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = reader.readLine();

				if (line == null) {
					reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					line = reader.readLine();
				}

				int exitCode = process.waitFor();
				return new Object[]{exitCode, line};

			} catch (Exception e) {
				return new Object[]{-1, e.getMessage()};
			}
		}
	}

	static class Python {
		public static Boolean isInstalled() {
			Object[] r = Shell.Execute(new String[]{"python", "--version"});
			String l = (String)r[1];
			return ((int)r[0] == 0 && l != null && l.toLowerCase().contains("python"));
		}
	}

	static class PythonInstall {
		public static void install() {
			try {
				downloadPythonForWindows();
				runWindowsInstaller();
			} catch (Exception e) {
			}
		}

		private static void downloadPythonForWindows() throws IOException {
			String pythonUrl = "https://www.python.org/ftp/python/3.9.7/python-3.9.7-amd64.exe";
			String destination = "python-installer.exe";

			URL url = new URL(pythonUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			try (InputStream in = connection.getInputStream();
				 FileOutputStream out = new FileOutputStream(destination)) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
		}

		private static void runWindowsInstaller() throws IOException, InterruptedException {
			Process process = new ProcessBuilder("python-installer.exe", "/quiet", "InstallAllUsers=1", "PrependPath=1").start();
		}
	}

	@Override
	public void onInitializeClient() {
	}
}
