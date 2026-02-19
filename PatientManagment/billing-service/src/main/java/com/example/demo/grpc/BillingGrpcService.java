package com.example.demo.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
      log.info("Creating BillingRequest {}", request.toString());

      BillingResponse response = BillingResponse.newBuilder()
          .setAccountId("1234")
          .setStatus("Active")
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
        
    }
}
