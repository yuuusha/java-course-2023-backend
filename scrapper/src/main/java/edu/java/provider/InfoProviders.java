package edu.java.provider;

import edu.java.provider.api.InfoProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InfoProviders {
    private final List<InfoProvider> infoProviders;

    public InfoProvider getSupplierByTypeHost(String typeHost) {
        for (InfoProvider infoProvider : infoProviders) {
            if (typeHost.equals(infoProvider.getSource())) {
                return infoProvider;
            }
        }
        return null;
    }
}
