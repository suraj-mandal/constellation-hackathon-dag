package com.my.currency.l0

import cats.effect.{Async, IO}
import org.tessellation.BuildInfo
import org.tessellation.currency.dataApplication.BaseDataApplicationL0Service
import org.tessellation.currency.l0.CurrencyL0App
import org.tessellation.currency.schema.currency.{
    CurrencyBlock,
    CurrencyIncrementalSnapshot,
    CurrencySnapshotStateProof,
    CurrencyTransaction
}
import org.tessellation.schema.address.Address
import org.tessellation.schema.balance.Balance
import org.tessellation.schema.cluster.ClusterId
import org.tessellation.schema.transaction.{
    RewardTransaction,
    TransactionAmount
}
import org.tessellation.sdk.domain.rewards.Rewards
import org.tessellation.sdk.infrastructure.consensus.trigger.ConsensusTrigger
import org.tessellation.security.SecurityProvider
import org.tessellation.security.signature.Signed

import eu.timepit.refined.auto._
import cats.syntax.applicative._

import java.util.UUID
import scala.collection.immutable.{SortedSet, SortedMap}

object RewardsMintForPredefinedAddresses {
    def make[F[_]: Async] =
        new Rewards[
          F,
          CurrencyTransaction,
          CurrencyBlock,
          CurrencySnapshotStateProof,
          CurrencyIncrementalSnapshot
        ] {
            def distribute(
                            lastArtifact: Signed[CurrencyIncrementalSnapshot],
                            lastBalances: SortedMap[Address, Balance],
                            acceptedTransactions: SortedSet[Signed[CurrencyTransaction]],
                            trigger: ConsensusTrigger
                          ): F[SortedSet[RewardTransaction]] = SortedSet(
                Address("DAG8pkb7EhCkT3yU87B2yPBunSCPnEdmX2Wv24sZ"),
                Address("DAG4o41NzhfX6DyYBTTXu6sJa6awm36abJpv89jB")
            ).map(RewardTransaction(_, TransactionAmount(55_500_0000L))).pure[F]
        }
}
object Main
  extends CurrencyL0App(
    "hackathon-dag-l0",
    "hackathon-dag L0 node",
    ClusterId(UUID.fromString("517c3a05-9219-471b-a54c-21b7d72f4ae5")),
    tessellationVersion = TessellationVersion.unsafeFrom(BuildInfo.version),
    metagraphVersion = MetagraphVersion.unsafeFrom(BuildInfo.version)
  ) {
}
