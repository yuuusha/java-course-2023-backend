package edu.java.supplier;

import edu.java.supplier.api.InfoSupplier;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InfoSuppliers {
    private final List<InfoSupplier> infoSuppliers;

    public InfoSupplier getSupplierByTypeHost(String typeHost) {
        for (InfoSupplier infoSupplier : infoSuppliers) {
            if (typeHost.equals(infoSupplier.getTypeSupplier())) {
                return infoSupplier;
            }
        }
        return null;
    }
}
