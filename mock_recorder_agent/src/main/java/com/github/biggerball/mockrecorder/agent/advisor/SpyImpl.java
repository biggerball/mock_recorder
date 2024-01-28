package com.github.biggerball.mockrecorder.agent.advisor;


import com.github.biggerball.mockrecorder.agent.entity.InvokeRecord;
import com.github.biggerball.mockrecorder.agent.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpyImpl {

    private static final ThreadLocal<Stack<InvokeRecord>> invokeStack = new ThreadLocal<>();
    private static final Map<Class<?>, String> classInterfaces = new ConcurrentHashMap<>();

    protected static final String traceId = UUID.randomUUID().toString();

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();


    public static void atEnter(Class<?> clazz, String methodInfo, Object target, Object[] args) {

        String[] info = StringUtils.splitMethodInfo(methodInfo);
        String methodName = info[0];

        Stack<InvokeRecord> invokeRecords = initInvokeStack();

        InvokeRecord invokeRecord = new InvokeRecord(
                traceId,
                clazz.getName() + "|" + findInterfaces(clazz),
                methodName,
                System.currentTimeMillis()
        );
        try {
            for (Object arg : args) {
                if (arg == null) {
                    invokeRecord.addParameter("null", "-");
                } else {
                    invokeRecord.addParameter(arg.getClass().getName(), gson.toJson(arg));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        invokeRecords.push(invokeRecord);
    }

    public static void atExit(Object returnObject) {
        Optional<InvokeRecord> recordOptional = getInvokeRecord();
        if (!recordOptional.isPresent()) {
            return;
        }
        InvokeRecord invokeRecord = recordOptional.get();
        try {
            if (returnObject != null) {
                invokeRecord.addReturnValue(returnObject.getClass().getName(), gson.toJson(returnObject));
            } else {
                invokeRecord.addReturnValue("null", "-");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        HttpSender.send(gson.toJson(invokeRecord));
    }


    public static void atExceptionExit(Throwable throwable) {
        Optional<InvokeRecord> recordOptional = getInvokeRecord();
        if (!recordOptional.isPresent()) {
            return;
        }
        InvokeRecord invokeRecord = recordOptional.get();
        try {
            invokeRecord.addException(throwable.getClass().getName(), gson.toJson(throwable));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        HttpSender.send(gson.toJson(invokeRecord));
    }

    private static Stack<InvokeRecord> initInvokeStack() {
        Stack<InvokeRecord> invokeRecords = invokeStack.get();
        if (invokeRecords == null) {
            invokeRecords = new Stack<>();
            invokeStack.set(invokeRecords);
        }
        return invokeRecords;
    }

    private static Optional<InvokeRecord> getInvokeRecord() {
        Stack<InvokeRecord> invokeRecords = invokeStack.get();
        if (invokeRecords == null) {
            System.out.println("invokeStack null");
            return Optional.empty();
        }
        InvokeRecord invokeRecord = invokeRecords.pop();
        if (invokeRecord == null) {
            System.out.println("stack null");
            return Optional.empty();
        }
        return Optional.of(invokeRecord);
    }

    private static String findInterfaces(Class<?> clazz) {
        String interfacesStr = classInterfaces.get(clazz);
        if (interfacesStr != null) {
            return interfacesStr;
        }

        StringBuilder sb = new StringBuilder();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            sb.append(interfaces[i].getName());
            if (i != interfaces.length - 1) {
                sb.append("\n");
            }
        }
        interfacesStr = sb.toString();
        classInterfaces.put(clazz, interfacesStr);
        return interfacesStr;
    }
}