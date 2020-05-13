package com.redhat.demo.voting.cache;

import com.redhat.demo.voting.model.Poll;
import com.redhat.demo.voting.model.PollOption;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(
        includeClasses = {
                Poll.class,
                PollOption.class
        },
        schemaFileName = "poll.proto",
        schemaFilePath = "proto/",
        schemaPackageName = "polls")
public interface PollsSerializationContextInitializer extends  SerializationContextInitializer {
}
