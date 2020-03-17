/*
 * This file displays the steps of the analysis
 * (Queued, Preparing, Submitting, Running,
 * Completing, Completed)
 */

/*
 * The following import statements makes available all the elements
 * required by the component
 */
import React, { useContext } from "react";
import { Steps } from "antd";
import { AnalysisContext, stateMap } from "../../../contexts/AnalysisContext";

import { SPACE_MD } from "../../../styles/spacing";
import { Running } from "../../../components/icons/Running";

import {
  getHumanizedDuration
} from "../../../utilities/date-utilities";

const Step = Steps.Step;

export function AnalysisSteps() {
  const { analysisContext } = useContext(AnalysisContext);

  const analysisDuration = getHumanizedDuration({ date: analysisContext.duration });
  const analysisError = analysisContext.isError;
  const previousState = analysisContext.previousState;
  const analysisState = analysisContext.analysisState;

  return (
    <Steps
      current={
        analysisError
          ? stateMap[previousState]
          : stateMap[analysisState]
      }
      status={analysisError ? "error" : "finish"}
      style={{ paddingBottom: SPACE_MD, paddingTop: SPACE_MD }}
    >
      <Step
        title={i18n("AnalysisSteps.new")}
        icon={analysisState === "NEW" ? <Running /> : null}
        description={analysisState === "NEW" || (previousState === null || stateMap[previousState] === "NEW") && analysisError ? analysisDuration : null}
      />
      <Step
        title={i18n("AnalysisSteps.preparing")}
        icon={
          analysisState === "PREPARING" ? <Running /> : null
        }
        description={analysisState === "PREPARING" || (stateMap[previousState] === "PREPARING" && analysisError) ? analysisDuration : null}
      />
      <Step
        title={i18n("AnalysisSteps.submitting")}
        icon={
          analysisState === "SUBMITTING" ? <Running /> : null
        }
        description={analysisState === "SUBMITTING" || (stateMap[previousState] === "SUBMITTING" && analysisError) ? analysisDuration : null}
      />
      <Step
        title={i18n("AnalysisSteps.running")}
        icon={analysisState === "RUNNING" ? <Running /> : null}
        description={analysisState === "RUNNING" || (stateMap[previousState] === "RUNNING" && analysisError) ? analysisDuration : null}
      />
      <Step
        title={i18n("AnalysisSteps.completing")}
        icon={
          analysisState === "COMPLETING" ? <Running /> : null
        }
        description={analysisState === "COMPLETING" || (stateMap[previousState] === "COMPLETING" && analysisError) ? analysisDuration : null}
      />
      <Step
        title={i18n("AnalysisSteps.completed")}
        icon={
          analysisState === "COMPLETED" ? <Success /> : null
        }
      />
    </Steps>
  );
}
