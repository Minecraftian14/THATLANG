package in.mcxiv.tryCatchSuite;

import com.mcxiv.logger.tools.LogLevel;

import java.util.Optional;
import java.util.function.Supplier;

import static in.mcxiv.tryCatchSuite.SupplierProvider.NULL;

public class Try {

    public static void Run(DangerousRunnable dangerousRunnable) {
        try {
            dangerousRunnable.run();
        } catch (Exception e) {
            LogLevel.DEBUG.act(e::printStackTrace);
        }
    }

    public static ExceptionConsumerProvider RunAnd(DangerousRunnable dangerousRunnable) {
        return exceptionConsumer -> {
            try {
                dangerousRunnable.run();
            } catch (Exception e) {
                exceptionConsumer.consume(e);
            }
        };
    }

    public static <ReturnType> ReturnType Get(DangerousSupplier<ReturnType> dangerousSupplier) {
        try {
            return dangerousSupplier.get();
        } catch (Exception e) {
            LogLevel.DEBUG.act(e::printStackTrace);
        }
        return null;
    }

    public static <ReturnType> SupplierProvider<ReturnType> GetAnd(DangerousSupplier<ReturnType> dangerousSupplier) {
        return returnTypeSupplier -> {
            try {
                return dangerousSupplier.get();
            } catch (Exception e) {
//                LogLevel.DEBUG.act(e::printStackTrace);
                return returnTypeSupplier.get();
            }
        };
    }

    public static <ReturnType> Optional<ReturnType> Opt(DangerousSupplier<ReturnType> dangerousSupplier) {
        return Optional.ofNullable(GetAnd(dangerousSupplier).Else(NULL()));
    }

    public static <ReturnType> SupplierProviderStep<ReturnType> If(DangerousSupplier<Boolean> condition) {
        return supplier -> returnTypeSupplier -> {
            try {
                if (condition.get()) return supplier.get();
            } catch (Exception ignored) {
            }
            return returnTypeSupplier.get();
        };
    }

    public static <T> T Throw(Supplier<RuntimeException> supplier) {
        throw supplier.get();
    }
}
