/*
 * Copyright (c) 2014-2016 by its authors. Some rights reserved.
 * See the project homepage at: https://monix.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package monix.benchmarks

import java.util.concurrent.TimeUnit

import monix.eval.Task
import org.openjdk.jmh.annotations._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/** Sample run:
  *
  *     sbt "benchmarks/jmh:run -i 10 -wi 10 -f 1 -t 1 monix.benchmarks.TaskFlatMapBenchmark"
  *
  * Which means "10 iterations" "10 warmup iterations" "1 fork" "1 thread".
  * Please note that benchmarks should be usually executed at least in
  * 10 iterations (as a rule of thumb), but more is better.
  */
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class TaskFlatMapBenchmark {
  @Benchmark
  def sequenceA(): Long = {
    import monix.execution.Scheduler.Implicits.global
    val tasks = (0 until 10000).map(_ => Task(1)).toList
    val f = Task.sequence(tasks).map(_.sum.toLong).runAsync
    Await.result(f, Duration.Inf)
  }

  @Benchmark
  def sequenceS(): Long = {
    import monix.execution.Scheduler.Implicits.global
    val tasks = (0 until 10000).map(_ => Task.eval(1)).toList
    val f = Task.sequence(tasks).map(_.sum.toLong).runAsync
    Await.result(f, Duration.Inf)
  }
}
