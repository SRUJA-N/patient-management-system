package com.example.demo.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
      log.info("Creating BillingRequest {}",BillingRequest.toString());

      BillingResponse response=BillingResponse.toBuilder().setAccountId("1234").setStatus("Active").build();
      responseObserver.Onnext(patient);
      responseObserver.OnCompleted();
        
    }
}
