package com.sss.restcontroller;

import com.sss.service.DinoService;
import com.sss.snapshot.DinoSnapshot;
import com.sss.snapshot.DinoSnapshotCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = {"/dinosaur"})
public class DinoController {
    private static final Logger logger = LogManager.getLogger();
    @Autowired
    private DinoService dinoService;
//    get /dinos
//    post /dinos
//    get /dinos/{id}
//    put /dinos/{id}
//    delete /dinos/{id}


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<DinoSnapshotCollection> getDinos(
            @RequestHeader Map<String, String> headers,
            @RequestParam(value = "start", defaultValue = "0") long start,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<DinoSnapshot> fetch = dinoService.fetch();
        DinoSnapshotCollection dinoSnapshotCollection = new DinoSnapshotCollection(start, fetch.size(), limit, fetch);
        return ResponseEntity.ok(dinoSnapshotCollection);
    }


//    @Operation(summary = "Crate a new order")
//    @ApiResponse(responseCode = "201", description = "Order is created", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrderDto.class))})
//    @PostMapping(consumes = APPLICATION_JSON_VALUE)
//    public ResponseEntity<OrderDto> createOrder(
//            @Valid @RequestBody OrderIncomingDto orderIncomingDto) {
//        final OrderDto createdOrder = orderService.createOrder(orderIncomingDto);
//        logger.info(NEW_ORDER_LOG, createdOrder.toString());
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
//    }
//


}
