package com.atlan.service;

import com.atlan.model.MetadataAsset;
import org.springframework.stereotype.Service;

@Service
public class GovernanceService {

    public void applyGovernancePolicies(MetadataAsset metadataAsset) {
        System.out.println("Applying Governance Policies for: " + metadataAsset.getQualifiedName() + " - Type: " + metadataAsset.getType());


        if (metadataAsset.getAttributes().containsKey("pii") || metadataAsset.getAttributes().containsKey("gdpr")) {
            enforceAccessControl(metadataAsset);
        }
    }

    private void enforceAccessControl(MetadataAsset metadataAsset) {
        System.out.println("Enforcing Access Control for PII/GDPR annotated asset: " + metadataAsset.getQualifiedName());
    }
}