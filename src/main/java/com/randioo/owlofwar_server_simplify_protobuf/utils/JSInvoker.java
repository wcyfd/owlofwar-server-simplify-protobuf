package com.randioo.owlofwar_server_simplify_protobuf.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSInvoker {
	private ScriptEngine engine;
	private String fileName = null;

	public JSInvoker() {
		ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("js");
	}

	private void init() {
		File file = new File(fileName);
		try(FileReader reader = new FileReader(file)) {
			engine.eval(reader);			
		} catch (ScriptException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Object invoke(String functionName, Object... param) {
		try {
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				Object obj = invoke.invokeFunction(functionName, param);
				return obj;
			}

		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
		return null;
	}
}